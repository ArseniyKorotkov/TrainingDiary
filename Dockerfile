FROM maven:3.9.1

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

ENTRYPOINT ["java","-jar","target/TrainingDiary-1.0-SNAPSHOT-shaded.jar"]