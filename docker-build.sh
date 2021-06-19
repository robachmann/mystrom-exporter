#!/usr/bin/env bash
rm -rf build/libs
./gradlew assemble
docker build --platform linux/arm64 -t robachmann/mystrom-legacy-exporter:1.1.1-arm64 .
docker push robachmann/mystrom-legacy-exporter:1.1.1-arm64