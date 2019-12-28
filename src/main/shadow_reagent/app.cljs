(ns shadow-reagent.app
  (:require [reagent.core :as r]))

(defn app []
  [:div#app
   [:h1 "shadow-cljs reagent template"]])

(defn init []
  (r/render [app]
            (.getElementById js/document "root")))