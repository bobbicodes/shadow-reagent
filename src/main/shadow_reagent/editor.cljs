(ns shadow-reagent.editor
  (:require ["@codemirror/fold" :as fold]
            ["@codemirror/closebrackets" :refer [closeBrackets]]
            ["@codemirror/gutter" :refer [lineNumbers]]
            ["@codemirror/highlight" :as highlight]
            ["@codemirror/history" :refer [history historyKeymap]]
            ["@codemirror/state" :refer [EditorState EditorSelection]]
            ["@codemirror/view" :as view :refer [EditorView]]
            [clojure-eval.sci :as sci]
            [clojure.string :as str]
            [applied-science.js-interop :as j]
            [nextjournal.clojure-mode :as cm-clj]
            [nextjournal.clojure-mode.live-grammar :as live-grammar]))

(def theme
  (.theme
   EditorView
   (j/lit {".cm-content" {:white-space "pre-wrap", :padding "10px 0"}
           "&.cm-focused" {:outline "none"}
           ".cm-line" {:padding "0 10px"
                       :line-height "1.6"
                       :font-size "16px"
                       :font-family "var(--code-font)"}
           ".cm-matchingBracket" {:border-bottom "2px solid var(--black)"
                                  :color "inherit"}
           ".cm-gutters" {:background "transparent", :border "none"}
           ".cm-gutterElement" {:margin-left "5px"}
           ;; only show cursor when focused
           ".cm-cursor" {:visibility "hidden"}
           "&.cm-focused .cm-cursor" {:visibility "visible"}})))

(defonce extensions
  #js
   [theme
    (history)
    highlight/defaultHighlightStyle
    (view/drawSelection)
    (lineNumbers)
    (closeBrackets)
    (fold/foldGutter)
    (.. EditorState -allowMultipleSelections (of true))
    (if false
     ;; use live-reloading grammar
      #js [(cm-clj/syntax live-grammar/parser)
           (.slice cm-clj/default-extensions 1)]
      cm-clj/default-extensions)
    (.of view/keymap cm-clj/complete-keymap)
    (.of view/keymap historyKeymap)])

(defn make-state [extensions doc]
  (let [[doc ranges] (->> (re-seq #"\||<[^>]*?>|[^<>|]+" doc)
                          (reduce (fn [[^string doc ranges] match]
                                    (cond (= match "|")
                                          [(str doc) (conj ranges (.cursor EditorSelection (count doc)))]
                                          (str/starts-with? match "<")
                                          [(str doc (subs match 1 (dec (count match))))
                                           (conj ranges (.range EditorSelection (count doc)
                                                                (+ (count doc) (- (count match) 2))))]
                                          :else
                                          [(str doc match) ranges])) ["" []]))]
    (.create EditorState
             #js{:doc doc
                 :selection
                 (if (seq ranges)
                   (.create EditorSelection (to-array ranges))
                   js/undefined)
                 :extensions
                 (cond-> #js[(.. EditorState -allowMultipleSelections (of true))]
                   extensions
                   (j/push! extensions))})))

(defn inlineEval [source]
  (new EditorView
       (j/obj :state
              (make-state
               (.concat #js[extensions] #js[(sci/extension)])
               source)
              :parent (js/document.querySelector "#app"))))
