#!/usr/bin/env bash
rm -rf build/libs
./gradlew assemble
docker build --platform linux/arm64 -t robachmann/mystrom-exporter:1.2.1-arm64 .
docker push robachmann/mystrom-exporter:1.2.1-arm64
