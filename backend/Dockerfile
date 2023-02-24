 # build image
FROM maven:3.8.3-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn package

# runtime image
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/vending-machine.jar .
CMD ["java", "-jar", "vending-machine.jar"]