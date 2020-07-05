FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /opt
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8083