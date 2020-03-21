(let [file-in (first *command-line-args*)
      file-out (second *command-line-args*)]
  (spit file-out (str (slurp file-in) " World!\n")))
