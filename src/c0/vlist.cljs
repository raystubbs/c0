(ns c0.vlist
  (:require
   [zero.core :refer [<< <<ctx act] :as z]
   [zero.config :as zc]
   [zero.dom :as zd]
   [c0.style :refer [common-style]]
   [c0.sizer]
   [goog.object :as obj]))

(defonce ^:private private-state-sym (js/Symbol (str ::private)))

(zc/reg-effects
  ::-connect
  (fn [^js/HTMLElement host]
    (let [resize-observer
          (js/ResizeObserver.
            (fn [entries]
              (doseq [^js entry entries]
                (zd/patch-internal-state!
                  host
                  {:path [:list-height]
                   :change [:value (-> entry .-contentRect .-height)]}))))]
      (.observe resize-observer host)
      (obj/set host private-state-sym resize-observer)))

  ::-disconnect
  (fn [^js/HTMLElement host]
    (when-let [resize-observer ^js (-> (obj/get host private-state-sym) :resize-observer)]
      (.disconnect resize-observer))))

(defn view
  [{:keys [item-height items] {:keys [list-height scroll item-height-px]} :state :as props}]
  {:pre [(string? item-height) (vector? items)]}
  [:root>
   :#on {:connect (act [::-connect (<<ctx ::z/host)])
         :disconnect (act [::-disconnect (<<ctx ::z/host)])}
   :#on-host {:scroll
              (fn [^js/Event event]
                (when (= (.-target event) (.-currentTarget event))
                  (zd/patch-internal-state!
                    (.-target event)
                    {:path [:scroll]
                     :change [:value (.-scrollTop (.-target event))]})))}
   :#style {:display :block
            :width "100%"
            :overflow-y :auto
            :position :relative
            :max-height "100%"
            :height "50rem"}
   :#css [common-style]
   [:c0/sizer
    :size item-height
    :#on {:size
          (act [::zd/patch-internal-state (<<ctx ::z/host)
                {:path [:item-height-px]
                 :change [:value (<<ctx ::z/data :height)]}])}]
   [:div
    :#style {:height (str "calc(" (count items) " * " item-height ")")}]
   (when (and (number? item-height-px) (number? list-height))
     (let [num-items-per-window (quot list-height item-height-px)
           window-start-idx (min (count items) (js/Math.ceil (/ (or scroll 0) item-height-px)))
           window-end-idx (min (count items) (+ window-start-idx num-items-per-window))]
       [:div.absolute
        :#style {:width "100%"
                 :height (str (* num-items-per-window item-height-px) "px")
                 :top (str (* window-start-idx item-height-px) "px")}
        (when (and (pos? window-start-idx) (pos? (count items)))
          [:div.absolute
           :#style {:width "100%"
                    :height (str item-height-px "px")
                    :top (str "-" item-height-px "px")}
           (get items (dec window-start-idx))])
        (map-indexed
          (fn [idx item]
            [:div
             :#key (or (some-> item meta :key) [::key idx])
             :#style {:height item-height :overflow :hidden}
             item])
          (subvec items window-start-idx window-end-idx))
        (when (and (< window-end-idx (count items)) (pos? (count items)))
          [:div.absolute
           :#style {:width "100%"
                    :height (str item-height-px "px")
                    :top "100%"}
           (get items window-end-idx)])]))])


(zc/reg-components
  :c0/vlist
  {:view view
   :props {:item-height :default
           :items :default
           :state (zd/internal-state-prop {:scroll 0})}})
