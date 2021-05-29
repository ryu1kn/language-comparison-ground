# test-runner design

Test runner runs all the exercise for all the language solutions.

Exercise directory structure looks like this:

    problems/
      hello-world/      # - This contains one test directory and solution directories (with probably language names)
        _test/          # - This contains test cases
          README.md     # - Exercise description
          test.sh       # - Script to run the test against a language directory, e.g. `clojure`.
                        #   the test runner invokes this script once per solution directory
        clojure/        # - A solution directory. Its contents depends on what `_test/test.sh` expects.
          ...           #   Also probably depends on how the solution is made of (e.g. language)
        javascript/     # - Another solution directory.
        ...

The test for the above `hello-world` exercise is invoked by the test runner as follows:

```sh
# current directory is `problems/hello-world/_test`.
# The test can assume that there is `../clojure` directory.
bash test.sh clojure
```

`test.sh` needs to:

* When the test passed, exit with 0.
* When the test failed, exit with non-0.
* When the test failed, provide the failure reason to `stderr`.
