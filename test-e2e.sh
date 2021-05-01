#!/usr/bin/env bash

set -euo pipefail

assert() {
    [[ "$1" != "$2" ]] && { echo "Expected \"$2\", but got \"$1\"."; exit 1; }
}

readonly message="$(lein all-test)"
readonly exit_code="$?"

assert "$message" foo
assert "$exit_code" 1
