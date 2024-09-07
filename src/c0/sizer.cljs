(ns c0.sizer
  (:require
   [zero.core :refer [act <<ctx] :as z]
   [zero.config :as zc]
   [goog.object :as obj]))

(defonce ^:private private-state-sym (js/Symbol (str ::private)))

(zc/reg-effects
  ::-connect
  (fn [^js/HTMLElement host]
    (let [resize-observer
          (js/ResizeObserver.
            (fn [entries]
              (doseq [^js entry entries
                      :let [rect (.-contentRect entry)]]
                (.dispatchEvent host
                  (js/CustomEvent. "size" #js{:detail {:width (.-width rect) :height (.-height rect)}})))))]
      (.observe resize-observer (-> host .-shadowRoot .-firstChild))
      (obj/set host private-state-sym {:resize-observer resize-observer})))

  ::-disconnect
  (fn [^js/HTMLElement host]
    (when-let [resize-observer ^js (-> (obj/get host private-state-sym) :resize-observer)]
      (.disconnect resize-observer))))

(defn view
  [{:keys [size]}]
  {:pre [(string? size)]}
  [:root>
   :#on {:connect (act [::-connect (<<ctx ::z/host)])
         :disconnect (act [::-disconnect (<<ctx ::z/host)])}
   :#style {:width "1px"
            :height "1px"
            :position :fixed
            :top "100vw"
            :left "0px"
            :overflow :scroll
            :pointer-events :none
            :visibility :hidden}
   [:div
    :#style {:width size :height size}]])

(zc/reg-components
  :c0/sizer
  {:view view
   :props #{:size}})
