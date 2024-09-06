(ns c0.input.text
  (:require
   [zero.core :refer [act << <<ctx] :as z]
   [zero.dom :as zd]
   [zero.config :as zc]
   [clojure.string :as str]
   [c0.style :refer [common-style]]))

(defn view
  [{:keys [value hint label disabled char-regex max-length] {:keys [focused? changed?] :as state} :state}]
  (let [error? (instance? js/Error value)
        outline-color (cond
                        error? "var(--c0-color-border-error)"
                        focused? "var(--c0-color-border-accent)"
                        :else "var(--c0-color-border-mid-res)")
        border (str "1px solid " outline-color)
        error-message (when error? (.-message ^js/Error value))
        display-value (if error?
                        (or (some-> value ex-data :input/value) "")
                        (str value))
        float-label? (or focused? (not (str/blank? display-value)))
        char-regex (if (string? char-regex) (js/RegExp. char-regex) char-regex)]
    [:root>
     :#css [common-style]
     :#internals {:#value display-value
                  :#validity (when error? {:flags #{:bad-input} :message error-message})}
     :#style {:display :block
              :width "15rem"
              :font-size "var(--c0-size-text-md)"}
     [:div.relative.w-full
      [:input.w-full.py-md.px-lg
       :#style {:color "var(--c0-color-text-high-res)"
                :font-size "var(--c0-size-text-md)"
                :background-color (if disabled "var(--c0-color-bg-field-disabled)" "var(--c0-color-bg-field)")
                :outline :none
                :border :none}
       :#on {:focus (act [::zd/patch-internal-state (<<ctx ::z/host) {:path [:focused?] :change [:value true]}])
             :blur (act
                     [::zd/patch-internal-state (<<ctx ::z/host) {:path [:focused?] :change [:value false]}]
                     (when changed?
                       [::zd/dispatch :value
                        :data (<< ::zd/field (<<ctx ::z/current) "value")])
                     (when (str/blank? display-value)
                       [::zd/set-field (<<ctx ::z/current) :value ""]))
             :beforeinput
             (fn [^js/InputEvent event]
               (when (or (some? char-regex) (some? max-length))
                 (case (.-inputType event)
                   ("insertText" "insertFromPaste")
                   (do
                     (.preventDefault event)
                     (let [target (.-target event)
                           data (if (some? char-regex)
                                  (->> event .-data (filter #(re-matches char-regex %)) (str/join ""))
                                  (.-data event))
                           value (str
                                   (subs (.-value target) 0 (.-selectionStart target))
                                   data
                                   (subs (.-value target) (.-selectionEnd target)))
                           value (if (some? max-length)
                                   (subs value 0 max-length)
                                   value)
                           next-pos (+ (.-selectionStart target) (count data))]
                       (when-not (= (.-value target) value)
                         (set! (.-value target) value)
                         (.setSelectionRange target next-pos next-pos)
                         (.dispatchEvent target (js/InputEvent. "input" event)))))
                   nil)))

             :keydown
             (when changed?
               (fn [^js/Event event]
                 (when (= (.-key event) "Enter")
                   (.dispatchEvent (-> event .-target .getRootNode .-host)
                     (js/CustomEvent. "value" #js{:detail (-> event .-target .-value)})))))

             :input
             (act {:stop-propagation? true}
               [::zd/patch-internal-state (<<ctx ::z/host) {:path [:changed?] :change [:value true]}]
               [::zd/dispatch :draft :data (<<ctx ::z/data)])}
       :id "text-field"
       :type "text"
       :disabled disabled
       :value display-value]
      [:div.no-pointer.absolute.inset-0.flex
       [:div.rl-md
        :#style {:border-top border
                 :border-bottom border
                 :border-left border
                 :width "var(--c0-size-space-md)"}]
       [:div.px-md.flex.items-center
        :#style {:border-bottom border
                 :border-top (when-not float-label? border)
                 :color "var(--c0-color-text-mid-res)"}
        [:label
         :for "text-field"
         :#style {:transform (when float-label? "translateY(-1rem)")
                  :transition "transform 300ms, font-size 300ms"
                  :color (cond
                           error? "var(--c0-color-text-error)"
                           focused? "var(--c0-color-text-accent)"
                           :else "var(--c0-color-text-mid-res)")
                  :font-size (when float-label? "var(--c0-size-text-sm)")}
         label]]
       [:div.rr-md
        :#style {:border-top border
                 :border-right border
                 :border-bottom border
                 :flex 1}]]]
     (cond
       error?
       [:div.px-md.my-sm
        :#style {:color "var(--c0-color-text-error)"
                 :font-size "var(--c0-size-text-sm)"}
        error-message]
       
       (some? hint)
       [:div.px-md.my-sm
        :#style {:color "var(--c0-color-text-mid-res)"
                 :font-size "var(--c0-size-text-sm)"}
        hint])]))


(zc/reg-components
  :c0.input/text
  {:view view
   :form-associated? true

   :props
   {:value :default
    :hint :default
    :label :default
    :disabled :default
    :char-regex :default
    :max-length :default
    :state (zd/internal-state-prop {:focused? false :changed? false})}})
