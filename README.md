# ServiceHealthCheckAppln

# Description
Service Health Application is a microservice-based application that monitors the health of different services in a distributed environment.
The application periodically checks the health status of each service and updates the status in a centralized database.

# Installation
To install the project on a local machine, follow these steps:

Clone the repository from GitHub.
Install Java 11 or later on your local machine.
Install the Maven build tool.
Open the project in your preferred IDE.
Build the project using Maven.
Run the application by executing the main class.

# Usage

To use the application, follow these steps:

Make sure the application is running.
Open a web browser and navigate to the application's home page.
View the status of each service in the dashboard.
Technologies used
The application is built using the following technologies:

Java
Spring Boot
Spring Cloud
Eureka
Feign
Maven

# Architecture

The Service Health Application is designed as a microservice-based architecture. The application consists of three microservices, a Eureka server, and a centralized database. The microservices communicate with each other using the Feign client and are registered with the Eureka server. The Eureka server acts as a service registry and facilitates service discovery. The centralized database is used to store the health status of each service.

# Configuration

To configure the application, you can modify the application.properties files for each microservice. The files contain properties for configuring the database connection, server port, and other settings. You can also modify the Feign clients to change the endpoints used to check the health of each service.
