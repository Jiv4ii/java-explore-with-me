FROM amazoncorretto:11-alpine-jdk
COPY http-server/target/*.jar stat.jar
ENTRYPOINT ["java","-jar","/stat.jar"]