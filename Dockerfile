FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /opt/app
COPY build/libs/*final.jar /opt/app/app.jar

RUN jar xvf app.jar > /dev/null

FROM eclipse-temurin:21-jre-alpine

WORKDIR /opt/app

ENV JDK_JAVA_OPTIONS="-XX:ActiveProcessorCount=2 -XX:MaxRAMPercentage=75 -XX:+CrashOnOutOfMemoryError"
ENV TZ="Europe/Warsaw"
EXPOSE 8065

COPY --from=builder /opt/app/BOOT-INF/lib /opt/app/BOOT-INF/lib
COPY --from=builder /opt/app/org /opt/app/org
COPY --from=builder /opt/app/META-INF /opt/app/META-INF
COPY --from=builder /opt/app/BOOT-INF/*.idx /opt/app/BOOT-INF/
COPY --from=builder /opt/app/BOOT-INF/classes /opt/app/BOOT-INF/classes

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
