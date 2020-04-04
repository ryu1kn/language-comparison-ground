(require '[clj-http.client :as client])

(client/post "http://localhost:8000"
             {:async? true
              :body "John"}
             (fn [response] (println (:body response)))
             (fn [e] (println (.getMessage e))))
