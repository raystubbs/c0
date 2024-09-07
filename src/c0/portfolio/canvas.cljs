(ns c0.portfolio.canvas
  (:require
   [zero.config :as zc]
   [zero.dom :as zd]
   [zero.wcconfig :as zwc]
   [c0.style :refer [common-style]]
   [subzero.rstore :as rstore]

   [c0.input.text]
   [c0.action.button]
   [c0.vlist]))

(defonce !db (rstore/rstore {}))

(zc/reg-effects
  :db/patch
  (fn [patch]
    (rstore/patch! !db patch)))

(zc/reg-streams
  :db/at
  (fn [rx & path]
    (let [watch-key (gensym)]
      (rx (get-in @!db path))
      (rstore/watch !db watch-key (vec path)
        (fn [_ v _]
          (rx v)))
      #(rstore/unwatch !db watch-key))))

(zc/reg-components
  :canvas/exec
  {:props #{:act}
   :view (fn [{action :act}] [:root> :#on {:connect action}])})

(defn init []
  (set! js/document.adoptedStyleSheets #js[common-style])
  (zc/install! zc/!default-db)
  (zwc/install! zc/!default-db)
  (zd/install! zc/!default-db))
