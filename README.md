# Offer service [![Build Status](https://travis-ci.org/grant-burgess/clean-architecture-example-java-spring-boot.svg?branch=master)](https://travis-ci.org/grant-burgess/clean-architecture-example-java-spring-boot)

_Dear stargazers, hope you like the project. Feel free to ask questions. I'll add some diagrams when I get a chance_


This is a REST based service that allows API consumers to create, cancel and query offers for merchants.

The project was developed using TDD. The system architecture is heavily inspired by Robert C. Martin's Clean Architecture, a use case driven approach. I developed this program with the mindset that this module is apart of a large system and attempted to make it as modular and pluggable as possible.

The APIs invoke a single **Use Case**. The **Use Case** constructs and acts over one to many **Entities** and in doing so also talks to a **Gateway** more commonly known as **Repository** or **Adapters**.
When an API needs to return a result, a **Presenter** is passed into the API. As we pass through the various layers we use separate models. **Use Cases** produce **Response Models**, **Presenters** produce **View Models**.

For the delivery mechanism I used Spring's REST controllers to deliver JSON

There are 4 public APIs
- Create Offer
```
POST /offer-service/api/v1/offers
{
  "name: "offer name",
  "description: "offer description",
  "price: "5.00",
  "currency: "GBP",
  "duration: {
    "startDate: "2018-01-01",
    "endDate: "2018-01-31"
  }
}

Response
HTTP Status 201 Created
{
  id: "12c3009d-046d-411c-85eb-6e379e2f5d2a"
}
```
- Cancel Offer 

There are various ways in which we could cancel an offer e.g. use the PATCH or DELETE HTTP method, I instead opted for a command style
```
POST /offer-service/api/v1/offers/{offer-id}/cancel

Response
HTTP Status 204 No Content
```
- Get Offers
```
GET  /offer-service/api/v1/offers`

Response
HTTP Status 200
offers: [
  {
    id: "12c3009d-046d-411c-85eb-6e379e2f5d2a",
    name: "offer name",
    description: "offer description",
    price: "5.00",
    currency: "GBP",
    duration: {
      startDate: "2018-01-01",
      endDate: "2018-01-31"
    },
    status: "ACTIVE|EXPIRED"
  },
  {
    id: "cc3453d7-6d75-422c-87da-37ee9f65bd11",
    ...
  },
  {
    id: "3f5f779c-b145-49fd-8a73-c8770fad072a",
    ...
  }
]

```

- Get Offer by ID
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
    startDate: "2018-01-01",
    endDate: "2018-01-31"
  },
  status: "ACTIVE|EXPIRED"
}

Or

HTTP Status 404 Not Found
```

## Project dependencies
This project uses Java 8 with Spring Boot, H2 database and Swagger for API documentation. 
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

View the [Swagger documentation](http://localhost:8080/swagger-ui.html#/offer)
