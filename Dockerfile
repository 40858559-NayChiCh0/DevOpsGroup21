FROM eclipse-temurin:25-jdk
COPY target/devops.jar /tmp/
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "devops.jar"]
