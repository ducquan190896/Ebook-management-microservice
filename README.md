# Ebook-management-microservice

This repository contains a microservices-based application to manage ebook record. The project includes API Gateway, Config Server, Discovery Server, and Ebook microservice. 

## Project Components

### Config Server

The Config Server centralizes configuration management for all microservices, simplifying application maintenance and consistency across environments.

### Discovery

The Discovery provides service registration and discovery, enabling communication within the microservices ecosystem, and monitoring microservices' health and status.
The base URL of the discovery is http://localhost:8761

### Gateway

The API Gateway provides the single entry point for all client requests, managing and routing them to the appropriate microservices.
The base URL of the gateway is http://localhost:8222

### Ebook Microservice

The Ebook Microservice is a Spring Webflux app and responsible for managing requests from clients, such as adding, updating, and retrieving book records.
The base URL of the Ebook microsevice is http://localhost:8090

## Run all services of the whole project

* Navigate to each service in the project including config-server, discovery, gateway, and ebook.

* Install dependencies for each service by running the command:

```bash
./mvnw clean install
```

* Start the server of each service by running the command:

```bash
./mvnw spring-boot:run
```

* To run the integration and unit tests of Ebook microservice, navigate to ebook microservice and run the command:

```bash
./mvnw test
```


## For The Ebook API documentation
```
- Swagger UI : "http://localhost:8090/webjars/swagger-ui/index.html"
```

## For The Eureka registry management to view all registered services
```
- Eureka registry UI : "http://localhost:8761/"
```