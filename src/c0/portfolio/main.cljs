(ns c0.portfolio.main
  (:require
   [zero.config :as zc]
   [zero.core :refer [bnd act <<ctx] :as z]
   [zero.tools.portfolio :refer [defscene]]
   [subzero.plugins.component-registry :as component-registry]
   [portfolio.ui :as ui]
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
  [:div
   :#style {:display :grid
            :grid-template-columns "repeat(auto-fit, 15rem)"
            :gap "1rem"}
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
