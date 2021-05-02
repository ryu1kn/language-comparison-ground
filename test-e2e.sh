#!/usr/bin/env bash

set -euo pipefail

assert() {
    if [[ "$1" != "$2" ]] ; then
        echo "Expected \"$2\", but got \"$1\"."
        exit 1
    fi
}

readonly message="$(lein run fixtures/problems-fail)"
readonly exit_code="$?"

assert "$message" "$(cat << EOF
{:exit 2, :err clojure test failed


bash test failed
}
EOF
)"

assert "$exit_code" 0
