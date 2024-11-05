FROM --platform=linux/amd64 openjdk:21
EXPOSE 8080
ADD backend/target/usv-monitor.jar usv-monitor.jar
ENTRYPOINT ["java", "-jar", "usv-monitor.jar"]