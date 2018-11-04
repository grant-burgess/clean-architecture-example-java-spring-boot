# Offer service [![Build Status](https://travis-ci.org/grant-burgess/offer-service.svg?branch=master)](https://travis-ci.org/grant-burgess/offer-service)

The Offer service is a REST based service that allows merchants to create, cancel and query offers.

The project was developed using TDD. The system architecture is heavily inspired by Robert C. Martin's Clean Architecture, a use case driven approach. I developed this program with the mindset that it is apart of a large system and attempted to make it as modular as possible.

The APIs invoke a single **Use Case**. The **Use Case** constructs and acts over one to many **Entities** and in doing so also talks to the **Repository**.
When an API needs to return a result, a **Presenter** is passed into the API.

There are 3 public APIs
- Create Offer
```
POST /offer-service/api/v1/offers
{
  name: "offer name",
  description: "offer description",
  price: "5.00",
  currency: "GBP",
  duration: {
    start-date: "2018-01-01",
    end-date: "2018-01-31"
  }
}

Response
HTTP Status 201 Created
{
  id: "12c3009d-046d-411c-85eb-6e379e2f5d2a"
}
```
- Cancel Offer 
```
POST /offer-service/api/v1/offers/{offer-id}/cancel

Response
HTTP Status 204 No Content
```
- Get Offer
```
GET  /offer-service/api/v1/offers/{offer-id}`

Response
HTTP Status 200
{
  id: "12c3009d-046d-411c-85eb-6e379e2f5d2a",
  name: "offer name",
  description: "offer description",
  price: "5.00",
  currency: "GBP",
  duration: {
    start-date: "2018-01-01",
    end-date: "2018-01-31"
  },
  status: "ACTIVE|EXPIRED"
}
```

## Project dependencies
This project uses Java 8 with Spring Boot and Swagger for API documentation. 
Tests were written with JUnit4. 

## Running the application
To build the project, run:

```shell
gradlew clean build
```


To run the application, from the root of the project run:
```shell
gradlew bootRun
```
