(ns codepastesite.handler
  (:use [ring.middleware.json :only [wrap-json-response wrap-json-params]]
        [ring.util.response :only [response]])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [codepastesite.db :as db]
            [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))

(def port (Integer. (or (System/getenv "PORT") "8080")))

(defroutes app-routes
  (GET "/" [] (slurp (io/resource "public/index.html")))
  (GET "/api/pastes" [] (response (db/find-all-pastes)))
  (GET "/api/pastes/:id" [id] (response (db/find-paste (Long. id))))
  (POST "/api/pastes" [title content syntax visibility expires]
        (response (db/create-paste title content syntax visibility expires)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-json-params
   (wrap-json-response
    (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))))

(defn -main []
  (db/migrate)
  (ring/run-jetty app {:port port :join? false}))
