FROM winamd64/openjdk:11 as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM winamd64/openjdk:11
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-XX:MinRAMPercentage=50.0", "-XX:MaxRAMPercentage=90.0", "org.springframework.boot.loader.JarLauncher"]