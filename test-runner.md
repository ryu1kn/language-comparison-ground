# test-runner design

Test runner will run all the exercise for all the language solutions.

Exercise directory structure looks like this:

    problems/
      hello-world/
        _test/
          README.md     # `hello-world` exercise description
          test.sh       # Execute a test for a given language directory. e.g. `clojure`
        clojure/
          run.sh        # Contents in each language directory
                        # depends on how test.sh wants to run a test
        javascript/

The test for the above `hello-world` exercise is invoked as follows:

```sh
# current directory is `problems/hello-world/_test`.
# The test can assume that there is `../clojure` directory.
bash test.sh clojure
```

`test.sh` needs to:

* When the test passed, exit with 0.
* When the test failed, exit with non-0.
* When the test failed, provide the failure reason to `stderr`.
