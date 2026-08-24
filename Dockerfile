FROM eclipse-temurin:21-jdk AS build

WORKDIR /build

COPY pom.xml pom.xml
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre AS run

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]