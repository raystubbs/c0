(ns c0.action.button
  (:require
   [zero.core :refer [<<ctx act css] :as z]
   [zero.dom :as zd]
   [zero.config :as zc]
   [c0.style :refer [common-style]]))

(def ^:private custom-style
  (css "
    @keyframes spin {
       from {
         rotate: 0deg;
       }
       to {
         rotate: 360deg;
       }
     }
    
    button.primary {
      background-color: var(--c0-color-bg-btn-primary);
      border: 1px solid var(--c0-color-border-btn-primary);
      color: var(--c0-color-text-btn-primary);
    }
    button.primary:hover:not([aria-disabled]) {
      background-color: var(--c0-color-bg-btn-primary-hovered);
    }
    button.simple {
      background-color: var(--c0-color-bg-btn-simple);
      border: 1px solid var(--c0-color-border-btn-simple);
      color: var(--c0-color-text-btn-simple);
    }
    button.simple:hover:not([aria-disabled]) {
      background-color: var(--c0-color-bg-btn-simple-hovered);
    }
    button.lowpro {
      background-color: var(--c0-color-bg-btn-lowpro);
      border: 1px solid var(--c0-color-border-btn-lowpro);
      color: var(--c0-color-text-btn-lowpro);
    }
    button.lowpro:hover:not([aria-disabled]) {
      background-color: var(--c0-color-bg-btn-lowpro-hovered);
    }
    button.danger {
      background-color: var(--c0-color-bg-btn-danger);
      border: 1px solid var(--c0-color-border-btn-danger);
      color: var(--c0-color-text-btn-danger);
    }
    button.danger:hover:not([aria-disabled]) {
      background-color: var(--c0-color-bg-btn-danger-hovered);
    }"))

(defn view
  [{:keys [variant status] {:keys [ripple-rect]} :state}]
  (let [disabled? (or (= status :pending) (= status :disabled))
        variant (or variant :primary)]
    [:root>
     :#css [common-style custom-style]
     :#style {:display :block}
     [:button.p-md.r-md.relative.w-full.h-full.center
      :aria-disabled (when disabled? true)
      :#class (case variant
                 (:primary :simple :lowpro :danger) variant
                 :primary)
      :#style {:filter (when disabled? "contrast(65%)")
               :opacity (when disabled? "75%")
               :cursor (when-not disabled? :pointer)
               :overflow :hidden
               :outline (when disabled? :none)}
      :#on {:click
            (if disabled?
              #(.stopPropagation ^js %)
              (fn [^js/MouseEvent event]
                (let [rect ^js/DOMRect (.getBoundingClientRect (.-currentTarget event))
                      x (- (.-clientX event) (.-x rect))
                      y (- (.-clientY event) (.-y rect))
                      sz (* 2 (max (.-width rect) (.-height rect)))]
                  (zd/patch-internal-state!
                    (-> event .-target .getRootNode .-host)
                    {:path [:ripple-rect]
                     :change [:value {:x x :y y :h sz :w sz}]}))))}
      (when (= status :pending)
        (let [border "1.5px solid currentColor"]
          [:div
           :#style {:border-radius "9999px"
                    :width "0.75em"
                    :height "0.75em"
                    :border-left border
                    :border-top border
                    :margin-right "var(--c0-size-space-md)"
                    :animation "500ms linear infinite normal spin"}]))
      [:slot]
      [:div.absolute
       :#style {:left (str (:x ripple-rect) "px")
                :top (str (:y ripple-rect) "px")
                :width (if ripple-rect (str (:w ripple-rect) "px") 0)
                :height (if ripple-rect (str (:h ripple-rect) "px") 0)
                :opacity (when-not ripple-rect 0)
                :background-color (case variant
                                    :simple "var(--c0-color-fx-btn-simple)"
                                    :lowpro "var(--c0-color-fx-btn-lowpro)"
                                    :danger "var(--c0-color-fx-btn-danger)"
                                    "var(--c0-color-fx-btn-primary)")
                :transition "width 300ms ease-in, height 300ms ease-in"
                :border-radius "100%"
                :transform "translate(-50%, -50%)"}
       :#on {:transitionend
             (act [::zd/patch-internal-state (<<ctx ::z/host)
                   {:path [:ripple-rect]
                    :change [:value nil]}])}]]]))

(zc/reg-components
  :c0.action/button
  {:view view
   :props {:variant :default
           :status :default
           :state (zd/internal-state-prop {})}})
