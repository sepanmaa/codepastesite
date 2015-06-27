(ns codepastesite.db
  (:import java.sql.Timestamp)
  (:require [clojure.java.jdbc :as jdbc]))

(def db-spec (or (System/getenv "DATABASE_URL")
                 "postgresql://localhost:5432/codepastesite"))

(defn translate-timestamps [paste]
  (if paste
    (let [exp-time (if (:expires_at paste) (.getTime (:expires_at paste)))
          exp (assoc paste :expires_at exp-time)]
      (assoc exp :created_at (.getTime (:created_at paste))))))

(defn create-paste [title content syntax visibility expiration]
  (let [created (java.time.LocalDateTime/now)
        expires (case expiration
                  "10min" (Timestamp/valueOf (.plusMinutes created 10))
                  "1d" (Timestamp/valueOf (.plusDays created 1))
                  "1w" (Timestamp/valueOf (.plusWeeks created 1))
                  "2w" (Timestamp/valueOf (.plusWeeks created 2))
                  nil)
        title-notempty (if (or (nil? title) (= (.trim title) "")) "Untitled" title)]
    (translate-timestamps
     (first (jdbc/insert! db-spec :pastes
                          {:title title-notempty
                           :content content
                           :syntax syntax
                           :visibility visibility
                           :created_at (Timestamp/valueOf created)
                           :expires_at expires})))))

(defmacro create-table [db-spec & body]
  `(jdbc/db-do-commands ~db-spec (jdbc/create-table-ddl ~@body)))

(defn migrate []
  (let [count-tables "SELECT COUNT(*) FROM information_schema.tables 
                      WHERE table_name = 'pastes'"
        pastes (first (jdbc/query db-spec [count-tables]))]
    (println pastes)
    (when (= (:count pastes) 0)
      (create-table db-spec :pastes
                    [:id :serial :primary :key]
                    [:title :varchar :not :null :default "'Untitled'"]
                    [:content :text :not :null :default "''"]
                    [:syntax :varchar :not :null :default "'nohighlight'"]
                    [:visibility :varchar :not :null :default "'public'"]
                    [:created_at :timestamp :not :null :default :current_timestamp]
                    [:expires_at :timestamp]))))

(defn find-paste [id]
  (translate-timestamps
   (first (jdbc/query db-spec ["SELECT * FROM pastes WHERE id = ? 
                                AND (expires_at IS NULL OR expires_at > now())" id]))))


(defn find-all-pastes []
  (jdbc/query db-spec ["SELECT * FROM pastes 
                        WHERE (pastes.expires_at IS NULL OR pastes.expires_at > now()) 
                        AND visibility = 'public' 
                        ORDER BY pastes.created_at DESC LIMIT 10"]))


                                                                 
      
