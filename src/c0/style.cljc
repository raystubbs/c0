(ns c0.style
  (:require
   [zero.core :refer [css]]
   [subzero.logger :as log]
   [clojure.string :as str]))

(def colors
  {:surface/primary
   {:border-low-con {:light "#E7E5E4" :dark "#44403C"}
    :border-mid-con {:light "#D6D3D1" :dark "#78716C"}
    :border-high-con {:light "#A8A29E" :dark "#D6D3D1"}
    :border-accent {:light "#3B82F6" :dark "#3B82F6"}
    :border-error  {:light "#DC2626" :dark "#DC2626"}
    :text-low-con {:light "#D4D4D8" :dark "#57534E"}
    :text-mid-con {:light "#6B7280" :dark "#A8A29E"}
    :text-high-con {:light "#1F2937" :dark "#E7E5E4"}
    :text-accent {:light "#3B82F6" :dark "#3B82F6"}
    :text-error {:light "#DC2626" :dark "#DC2626"}
    :bg-field {:light "#FAFAF9" :dark "rgb(32, 33, 36)"}
    :bg-field-disabled {:light "rgba(239, 239, 239, 0.3)" :dark "rgba(59, 59, 59, 0.3)"}
    :bg-btn-primary {:light "#3B82F6" :dark "#3B82F6"}
    :bg-btn-simple {:light "transparent" :dark "transparent"}
    :bg-btn-lowpro {:light "transparent" :dark "transparent"}
    :bg-btn-danger {:light "#DC2626" :dark "#DC2626"}
    :bg-btn-primary-hovered {:light "#166bf5" :dark "#166bf5"}
    :bg-btn-simple-hovered {:light "rgba(0, 0, 0, 0.05)" :dark "rgba(255, 255, 255, 0.05)"}
    :bg-btn-lowpro-hovered {:light "rgba(0, 0, 0, 0.05)" :dark "rgba(255, 255, 255, 0.05)"}
    :bg-btn-danger-hovered {:light "#d91111" :dark "#d91111"}
    :text-btn-primary {:light "white" :dark "white"}
    :text-btn-simple {:light "#1F2937" :dark "#E7E5E4"}
    :text-btn-lowpro {:light "#6B7280" :dark "white"}
    :text-btn-danger {:light "white" :dark "white"}
    :border-btn-primary {:light "#1D4ED8" :dark "#1D4ED8"}
    :border-btn-simple {:light "transparent" :dark "transparent"}
    :border-btn-lowpro {:light "#A8A29E" :dark "#D6D3D1"}
    :border-btn-danger {:light "#7F1D1D" :dark "#7F1D1D"}
    :fx-btn-primary {:light "rgba(255, 255, 255, 0.25)" :dark "rgba(255, 255, 255, 0.25)"}
    :fx-btn-simple {:light "rgba(0, 0, 0, 0.25)" :dark "rgba(255, 255, 255, 0.25)"}
    :fx-btn-lowpro {:light "rgba(0, 0, 0, 0.25)" :dark "rgba(255, 255, 255, 0.25)"}
    :fx-btn-danger {:light "rgba(255, 255, 255, 0.25)" :dark "rgba(255, 255, 255, 0.25)"}
    :bg-surface {:light "#FAFAF9" :dark "rgb(32, 33, 36)"}}})

(def sizes
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
    (doseq [[color-name color] (:surface/primary colors)]
      (js/CSS.registerProperty
        #js{:name (str "--c0-color-" (name color-name))
            :syntax "<color>"
            :inherits true
            :initialValue (:light color)}))
    (doseq [[surface-name color-map] colors
            [color-name color-schemes] color-map
            [color-scheme color-val] color-schemes]
      (js/CSS.registerProperty
        #js{:name (str "--c0-" (name surface-name) "-" (name color-scheme) "-color-" (name color-name))
            :syntax "<color>"
            :inherits true
            :initialValue color-val}))
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
    .inline-flex {
      display: inline-flex;
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
  "))
