(ns c0.style
  (:require
   [zero.core :refer [css]]))

(def ^:private colors
  {:border-low-res "#E7E5E4"
   :border-mid-res "#D6D3D1"
   :border-high-res "#A8A29E"
   :border-accent "#3B82F6"
   :border-error "#DC2626"
   :text-low-res "#D4D4D8"
   :text-mid-res "#6B7280"
   :text-high-res "#1F2937"
   :text-accent "#3B82F6"
   :text-error "#DC2626"
   :bg-field "light-dark(white, rgb(32, 33, 36))"
   :bg-field-disabled "light-dark(rgba(239, 239, 239, 0.3), rgba(59, 59, 59, 0.3))"})

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
      (js/CSS.registerProperty
        #js{:name (str "--c0-color-" (name k))
            :syntax "<color>"
            :inherits false
            :initialValue v}))
    (doseq [[k v] sizes]
      (js/CSS.registerProperty
        #js{:name (str "--c0-size-" (name k))
            :syntax "<length>"
            :inherits false
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
  "))
