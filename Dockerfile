FROM adoptopenjdk/openjdk16-openj9:x86_64-alpine-jdk-16.0.1_9_openj9-0.26.0-slim
COPY build/libs/unraid-api-*-all.jar unraid-api.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-jar", "unraid-api.jar"]