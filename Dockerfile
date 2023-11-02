FROM adoptopenjdk/openjdk11
EXPOSE 8080
ADD target/*.jar spring-boot-elk-app.jar
ENTRYPOINT ["java","-jar","spring-boot-elk-app.jar"]
