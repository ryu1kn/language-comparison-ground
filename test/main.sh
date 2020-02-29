#!/bin/bash

set -euo pipefail

project_root="$(cd "$(dirname "$BASH_SOURCE")/.."; pwd)"
error_count=0

assert() {
    local actual="$1"
    local expected="$2"
    if [[ "$actual" != "$expected" ]] ; then
        echo "Expected \"$expected\", but got \"$actual\"" 1>&2
        error_count=$((error_count + 1))
    fi
}

for lang_dir in "$project_root"/topics/hello-world/* ; do
    (cd "$lang_dir" && assert "$(bash ./run.sh)" "Hello World!")
done

exit $error_count
