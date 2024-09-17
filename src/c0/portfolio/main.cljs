(ns c0.portfolio.main
  (:require
   [zero.config :as zc]
   [zero.core :refer [bnd act <<ctx] :as z]
   [zero.tools.portfolio :refer [defscene]]
   [subzero.plugins.component-registry :as component-registry]
   [portfolio.ui :as ui]
   [c0.style :refer [common-style]]
   [zero.cdf :as cdf]))

(defn init []
  (zc/install! zc/!default-db)
  (component-registry/reg-attribute-writers zc/!default-db
    :default
    (fn [v & _args]
      (cdf/write-str v)))
  (js/addEventListener "load"
    (fn []
      (ui/start!
        {:config {:canvas-path "/canvas.html"}}))))

(defscene text-input
  :collection :input
  :title "Text Input"
  [:c0/surface
   :variant :surface/primary
   :#style {:display :grid
            :grid-template-columns "repeat(auto-fit, 15rem)"
            :gap "1rem"
            :padding "1rem"}
   [:div
    [:c0.input/text
     :label "My Label"
     :#bind {:value (bnd :db/at 1 :value)}
     :#on {:value (act [:db/patch {:path [1 :value] :change [:value (<<ctx ::z/data)]}])
           :draft (act [:db/patch {:path [1 :draft] :change [:value (<<ctx ::z/data)]}])}]
    [:div
     :#style {:font-familty :mono :text-align :center}
     "draft: "
     [:code :#bind {:text-content (bnd :db/at 1 :draft)}]
     " | "
     "value: "
     [:code :#bind {:text-content (bnd :db/at 1 :value)}]]]
   [:div
    [:c0.input/text
     :label "Only Numbers"
     :char-regex "\\d"
     :#bind {:value (bnd :db/at 2 :value)}
     :#on {:value (act [:db/patch {:path [2 :value] :change [:value (<<ctx ::z/data)]}])
           :draft (act [:db/patch {:path [2 :draft] :change [:value (<<ctx ::z/data)]}])}]
    [:div
     :#style {:font-familty :mono :text-align :center}
     "draft: "
     [:code :#bind {:text-content (bnd :db/at 2 :draft)}]
     " | "
     "value: "
     [:code :#bind {:text-content (bnd :db/at 2 :value)}]]]
   [:c0.input/text
    :label "Always Errored"
    :value (ex-info "There's an error" {:input/value ""})]
   [:c0.input/text
    :label "Disabled"
    :disabled true
    :hint "This one's disabled"]])

(defscene action-button
  :collection :action
  :title "Action Button"
  [:c0/surface
   :variant :surface/primary
   :#style {:display :grid
            :grid-template-columns "repeat(auto-fit, 10rem)"
            :gap "1rem"
            :justify-items :center
            :padding "1rem"}
   [:c0.action/button
    "Primary Button"]
   [:c0.action/button
    :status :pending
    "Pending"]
   [:c0.action/button
    :status :disabled
    "Disabled"]
   [:c0.action/button
    :variant :simple
    "Simple"]
   [:c0.action/button
    :variant :lowpro
    "Low Profile"]
   [:c0.action/button
    :variant :danger
    "Danger"]])

(defscene virtual-list
  :title "Virtual List"
  :collection :util
  [:div
   "A Virtual List makes long lists more performant by only rendering "
   "the things that are visible in the viewport at any given time."
   [:c0/vlist
    :#style {:height "15rem" :width "10rem" :margin-top "1rem"}
    :item-height "1rem"
    :items (map
             (fn [n]
               (str "Item Number " n))
             (range 0 10000))]])

(defscene surfaces
  :title "Surfaces"
  :collection :styling
  [:c0/surface :variant :surface/primary
   [:div
    :#style {:display "grid"
             :grid-template-columns "1fr"
             :gap "1rem"
             :padding "1rem"}
    [:div :#style {:color "var(--c0-color-text-high-con)"}
     "High Contrast Text"]
    [:div :#style {:color "var(--c0-color-text-mid-con)"}
     "Mid Contrast Text"]
    [:div :#style {:color "var(--c0-color-text-low-con)"}
     "Low Contrast Text"]]
   [:div
    :#style {:display "grid"
             :grid-template-columns "1fr"
             :gap "1rem"
             :padding "1rem"}
    [:div
     :#style {:color "var(--c0-color-text-mid-con)"
              :border-bottom "1px solid var(--c0-color-border-high-con)"}
     "High Contrast Border"]
    [:div
     :#style {:color "var(--c0-color-text-mid-con)"
              :border-bottom "1px solid var(--c0-color-border-mid-con)"}
     "Mid Contrast Border"]
    [:div
     :#style {:color "var(--c0-color-text-mid-con)"
              :border-bottom "1px solid var(--c0-color-border-low-con)"}
     "Low Contrast Border"]]])
