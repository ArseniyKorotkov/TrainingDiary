FROM maven:3.9.1

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:latest

WORKDIR /app

COPY . .

CMD ["java","-jar","TrainingDiary.jar"]