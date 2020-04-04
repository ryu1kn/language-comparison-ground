(use 'ring.adapter.jetty)

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "Hello " (slurp (:body request)) "!")})

(run-jetty handler {:port  8000
                    :join? false})
