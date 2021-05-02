#!/usr/bin/env bash

set -euo pipefail

assert() {
    if [[ "$1" != "$2" ]] ; then
        echo "Expected \"$2\", but got \"$1\"."
        exit 1
    fi
}

set +e
message="$(lein run fixtures/problems-fail)"
exit_code="$?"
set -e

assert "$message" "$(cat << EOF
clojure test failed


bash test failed
EOF
)"

assert "$exit_code" 2
