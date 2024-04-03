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


Update datadog.yaml File:Create a datadog.yaml file on your host machine or update an existing one. This file will contain the Datadog Agent configuration settings.Add Configuration Settings:Open the datadog.yaml file in a text editor and add your configuration settings. Here are some common settings you might include:api_key: YOUR_API_KEY
logs_enabled: true
logs_config:
  container_collect_all: true
  enable_health_metrics: trueReplace YOUR_API_KEY with your Datadog API key.Adjust other settings such as log collection configurations (logs_config) according to your requirements.Mount datadog.yaml File into the Container:When running the Datadog Agent container, mount the datadog.yaml file into the container at the appropriate location.docker run -d --name datadog-agent \
-v /path/to/datadog.yaml:/etc/datadog-agent/datadog.yaml:ro \
-v /var/run/docker.sock:/var/run/docker.sock:ro \
-v /proc/:/host/proc/:ro \
-v /sys/fs/cgroup/:/host/sys/fs/cgroup:ro \
datadog/agent:latestReplace /path/to/datadog.yaml with the path to the datadog.yaml file on your host machine.Restart the Datadog Agent Container:After mounting the datadog.yaml file, restart the Datadog Agent container to apply the new configuration changes.docker restart datadog-agentVerify Configuration:Once the Datadog Agent container restarts, verify that the configuration settings are applied correctly by checking the Datadog dashboard or logs.
