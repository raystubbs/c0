(ns c0.style
  (:require
   [zero.core :refer [css]]
   [clojure.string :as str]))

(def ^:private colors
  {:border-low-res {:light "#E7E5E4" :dark "#292524"}
   :border-mid-res {:light "#D6D3D1" :dark "#44403C"}
   :border-high-res {:light "#A8A29E" :dark "#57534E"}
   :border-accent "#3B82F6"
   :border-error "#DC2626"
   :text-low-res "#D4D4D8"
   :text-mid-res "#6B7280"
   :text-high-res "#1F2937"
   :text-accent "#3B82F6"
   :text-error "#DC2626"
   :bg-field {:light "white" :dark "rgb(32, 33, 36)"}
   :bg-field-disabled {:light "rgba(239, 239, 239, 0.3)" :dark "rgba(59, 59, 59, 0.3)"}
   :bg-btn-primary "#3B82F6"
   :bg-btn-simple "transparent"
   :bg-btn-lowpro "transparent"
   :bg-btn-danger "#DC2626"
   :bg-btn-primary-hovered "#166bf5"
   :bg-btn-simple-hovered {:light "rgba(0, 0, 0, 0.05)" :dark "rgba(255, 255, 255, 0.05)"}
   :bg-btn-lowpro-hovered {:light "rgba(0, 0, 0, 0.05)" :dark "rgba(255, 255, 255, 0.05)"}
   :bg-btn-danger-hovered "#d91111"
   :text-btn-primary "white"
   :text-btn-simple {:light "#6B7280" :dark "white"}
   :text-btn-lowpro {:light "#6B7280" :dark "white"}
   :text-btn-danger "white"
   :border-btn-primary "#1D4ED8"
   :border-btn-simple "transparent"
   :border-btn-lowpro "#D6D3D1"
   :border-btn-danger "#7F1D1D"
   :fx-btn-primary "rgba(255, 255, 255, 0.25)"
   :fx-btn-simple {:light "rgba(0, 0, 0, 0.25)" :dark "rgba(255, 255, 255, 0.25)"}
   :fx-btn-lowpro {:light "rgba(0, 0, 0, 0.25)" :dark "rgba(255, 255, 255, 0.25)"}
   :fx-btn-danger "rgba(255, 255, 255, 0.25)"})

(def ^:private sizes
  {:radius-sm "2px"
   :radius-md "4px"
   :radius-lg "8px"
   :space-sm "4px"
   :space-md "8px"
   :space-lg "16px"
   :icon-sm "12px"
   :icon-md "16px"
   :icon-lg "20px"
   :text-sm "12px"
   :text-md "16px"
   :text-lg "20px"})

(defonce __register-custom-properties__
  (do
    (doseq [[k v] colors]
      (cond
        (string? v)
        (js/CSS.registerProperty
          #js{:name (str "--c0-color-" (name k))
              :syntax "<color>"
              :inherits true
              :initialValue v})

        (map? v)
        (do
          (js/CSS.registerProperty
            #js{:name (str "--c0-color-" (name k))
                :syntax "<color>"
                :inherits true
                :initialValue (:light v)})
          (js/CSS.registerProperty
            #js{:name (str "--c0-color-dark-" (name k))
                :syntax "<color>"
                :inherits true
                :initialValue (:dark v)})
          (js/CSS.registerProperty
            #js{:name (str "--c0-color-light" (name k))
                :syntax "<color>"
                :inherits true
                :initialValue (:light v)}))))
    (doseq [[k v] sizes]
      (js/CSS.registerProperty
        #js{:name (str "--c0-size-" (name k))
            :syntax "<length>"
            :inherits true
            :initialValue v}))))

(def common-style
  (css "
    * {
      box-sizing: border-box;
      font-family: \"Noto Sans\", \"Roboto\", sans-serif;
    }
    
    .no-pointer {
      pointer-events: none;
    }
    .inset-0 {
      inset: 0px;
    }
    .absolute {
      position: absolute;
    }
    .relative {
      position: relative;
    }
    .flex {
      display: flex;
    }
    .items-center {
      align-items: center;
    }
    .center {
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .w-screen {
      width: 100vw;
    }
    .h-screen {
      height: 100vh;
    }
    .w-full {
      width: 100%;
    }
    .h-full {
      height: 100%;
    }
    .my-sm {
      margin-top: var(--c0-size-space-sm);
      margin-bottom: var(--c0-size-space-sm);
    }
    .my-md {
      margin-top: var(--c0-size-space-md);
      margin-bottom: var(--c0-size-space-md);
    }
    .my-lg {
      margin-top: var(--c0-size-space-lg);
      margin-bottom: var(--c0-size-space-lg);
    }
    .mx-sm {
      margin-left: var(--c0-size-space-sm);
      margin-right: var(--c0-size-space-sm);
    }
    .mx-md {
      margin-left: var(--c0-size-space-md);
      margin-right: var(--c0-size-space-md);
    }
    .mx-lg {
      margin-left: var(--c0-size-space-lg);
      margin-right: var(--c0-size-space-lg);
    }
    .m-sm {
      margin: var(--c0-size-space-sm);
    }
    .m-md {
      margin: var(--c0-size-space-md);
    }
    .m-lg {
      margin: var(--c0-size-space-lg);
    }
    .py-sm {
      padding-top: var(--c0-size-space-sm);
      padding-bottom: var(--c0-size-space-sm);
    }
    .py-md {
      padding-top: var(--c0-size-space-md);
      padding-bottom: var(--c0-size-space-md);
    }
    .py-lg {
      padding-top: var(--c0-size-space-lg);
      padding-bottom: var(--c0-size-space-lg);
    }
    .px-sm {
      padding-left: var(--c0-size-space-sm);
      padding-right: var(--c0-size-space-sm);
    }
    .px-md {
      padding-left: var(--c0-size-space-md);
      padding-right: var(--c0-size-space-md);
    }
    .px-lg {
      padding-left: var(--c0-size-space-lg);
      padding-right: var(--c0-size-space-lg);
    }
    .p-sm {
      padding: var(--c0-size-space-sm);
    }
    .p-md {
      padding: var(--c0-size-space-md);
    }
    .p-lg {
      padding: var(--c0-size-space-lg);
    }
    .rr-sm {
      border-top-right-radius: var(--c0-size-radius-sm);
      border-bottom-right-radius: var(--c0-size-radius-sm);
    }
    .rr-md {
      border-top-right-radius: var(--c0-size-radius-md);
      border-bottom-right-radius: var(--c0-size-radius-md);
    }
    .rr-lg {
      border-top-right-radius: var(--c0-size-radius-lg);
      border-bottom-right-radius: var(--c0-size-radius-lg);
    }
    .rl-sm {
      border-top-left-radius: var(--c0-size-radius-sm);
      border-bottom-left-radius: var(--c0-size-radius-sm);
    }
    .rl-md {
      border-top-left-radius: var(--c0-size-radius-md);
      border-bottom-left-radius: var(--c0-size-radius-md);
    }
    .rl-lg {
      border-top-left-radius: var(--c0-size-radius-lg);
      border-bottom-left-radius: var(--c0-size-radius-lg);
    }
    .r-sm {
      border-radius: var(--c0-size-radius-sm);
    }
    .r-md {
      border-radius: var(--c0-size-radius-md);
    }
    .r-lg {
      border-radius: var(--c0-size-radius-lg);
    }
  "
    (str ":host {\n"
      (str/join "\n"
        (keep
         (fn [[k v]]
           (when (map? v)
             (str "  --c0-color-" (name k) ": light-dark(" (:light v) ", " (:dark v) ");")))
         colors)))))
