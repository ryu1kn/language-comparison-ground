FROM clojure:openjdk-8-tools-deps

COPY ./ /build/

WORKDIR /build/test

RUN curl -sL https://deb.nodesource.com/setup_12.x | bash - \
    && apt-get update -y \
    && apt-get install -y jq nodejs \
    \
    && wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein \
    && chmod +x lein \
    && mv lein /usr/local/bin \
    && lein deps
