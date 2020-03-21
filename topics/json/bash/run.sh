#!/bin/bash

set -euo pipefail

jq -c '.people |= map(. + {age: (.age + 1)})' "$1"
