# LENDING-CHALLENGE

## DATABASE
The Application run on H2 in-memory database, the configuration can be edited on the properties to run on mysql or oracle databases.

## ABOUT
This webservice provides APIs to control and monitor a Lending/Repayment engine.

It has APIs for creating and updating a subscriber, creating a loan and repaying a loan.

It also provides a notification service that sends out SMS messages when a loan is created or partial/full loan repayment is made.

A subscriber can take multiple loans

The application has JUnit5 unit tests. To test simply run `mvn clean test` on your commandline

## HOW TO RUN
Run the springboot application (`mvn spring-boot:run`)

Navigate to http://localhost:8080/swagger-ui/index.html on your browser for the detailed API documentation