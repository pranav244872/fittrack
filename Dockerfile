#####################################################
# Build Stage
#####################################################

FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

# Tells maven to look at pom.xml and download every library needed to build the app
RUN mvn dependency:go-offline

COPY src ./src

# compiles the code: clean: deletes any old build files, package: compiles the code and bundles it into a .jar file -DskipTests tells maven not to run unit tests now
RUN mvn clean package -DskipTests


#####################################################
# Runtime Stage
#####################################################

FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Tell the java container what to run as soon as it starts
ENTRYPOINT [ "java", "-jar", "app.jar" ]
