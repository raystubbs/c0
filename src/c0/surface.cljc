(ns c0.surface
  (:require
   [zero.config :as zc]
   [c0.style :refer [colors]]))


(defn- view
  [{:keys [variant]}]
  (let [color-map (get colors variant)]
    (when (nil? color-map)
      (throw (ex-info "Invalid surface variant" {:variant variant})))
    [:root>
     :#style (into
               {:display :block
                :background-color "var(--c0-color-bg-surface)"
                :color "var(--c0-color-text-high-con)"}
               (map
                 (fn [color-name]
                   [(keyword (str "--c0-color-" (name color-name)))
                    (str "light-dark(var(--c0-" (name variant) "-light-color-" (name color-name) "), "
                      "var(--c0-" (name variant) "-dark-color-" (name color-name) "))")])
                 (keys color-map)))
     [:slot]]))

(zc/reg-components
  :c0/surface
  {:view view
   :props #{:variant}})
