#!/bin/bash

set -euo pipefail

lein deps
lein test

(cd iteration-0 && lein deps && lein test)
