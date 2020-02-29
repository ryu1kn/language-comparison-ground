#!/bin/bash

set -euo pipefail

project_root="$(cd "$(dirname "$BASH_SOURCE")/.."; pwd)"
test_tmp_dir="$project_root/test/tmp"
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

for lang_dir in "$project_root"/topics/file-io/* ; do
    mkdir -p "$test_tmp_dir"
    output_filepath="$test_tmp_dir/file-io-output-$(basename "$lang_dir").txt"
    (cd "$lang_dir" && bash ./run.sh "$project_root/test/file-io-input.txt" "$output_filepath")
    assert "$(< "$output_filepath")" "Hello World!"
done

exit $error_count
