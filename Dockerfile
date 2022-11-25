#FROM openjdk:17-jdk-alpine
#WORKDIR /home/java/app
#COPY . .
#RUN ./mvnw spring-boot:run

FROM maven:3.8.5-openjdk-17
WORKDIR /home/java/app
COPY . .
RUN mvn clean install
CMD mvn spring-boot:run -X
