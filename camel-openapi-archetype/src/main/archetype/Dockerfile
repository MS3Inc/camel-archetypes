ARG CUSTOM_FROM=adoptopenjdk/openjdk11:alpine-jre

FROM ${CUSTOM_FROM} as builder
WORKDIR application
ARG JAR_FILE=maven/target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM ${CUSTOM_FROM}
WORKDIR application
RUN ls
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-XX:MinRAMPercentage=50.0", "-XX:MaxRAMPercentage=90.0", "org.springframework.boot.loader.JarLauncher"]