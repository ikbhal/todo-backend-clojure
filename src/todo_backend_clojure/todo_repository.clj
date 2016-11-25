(ns todo-backend-clojure.todo-repository
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname   "org.h2.Driver"
              :subprotocol "h2:file"
              :subname     "resources/db/todo;DB_CLOSE_DELAY=-1"})

(defn- sql-result->id [result-seq]
  (->
    result-seq
    first
    vals
    first))

(defn- as-todo [row]
  (dissoc (assoc row :order (:sequence row)) :sequence))

(defn- as-row [todo]
  (dissoc (assoc todo :sequence (:order todo)) :order))

(defn create-todo!
  "Creates a todo and returns the ID"
  [todo]
  (->
    (sql/insert! db-spec :todo todo)
    sql-result->id))

(defn get-all []
  (sql/query db-spec ["SELECT * FROM todo"]))

(defn get-by-id
  "Get todo by ID. Returns a list of todos"
  [id]
  (sql/query db-spec ["SELECT * FROM todo WHERE id = ?" id]))

(defn update-todo! [id todo]
  (sql/update! db-spec :todo todo ["id = ?" id])
  (get-by-id id))

(defn delete!
  "Delete the todo. Returns a list containing the ID of the deleted record"
  [id]
  (sql/delete! db-spec :todo ["id = ?" id]))

(defn delete-all! []
  (sql/delete! db-spec :todo [true]))
