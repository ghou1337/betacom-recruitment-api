# Recruitment project

This is a REST API microservice recruitment project
## Technologies

- Java 21 
- Spring Boot (3.5.4) 
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- jjwt(api, impl, jackson)
- MySQL
- H2 (testing)
- dotenv-java
- lombok
- Maven

## Accessing the Service
Once started, the microservice is accessible at:
- http://localhost:8080

## Endpoints
- POST /login – an unauthorized endpoint for logging in. Returns an authorization token in the response body.
- POST /register – an unauthorized endpoint for creating a user account by providing a username and password in the request body.
- POST /items – an authorized endpoint for creating a user item by providing its name in the request body.
- GET /items – an authorized endpoint for retrieving the list of the user's items.
## Start application

### 1. Clone repository
git clone https://github.com/ghou1337/betacom-recruitment-api.git

### 2. application.properties configuration

```properties
spring.application.name=Betacom
spring.datasource.url=${DB_URL}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET}
```
### 2.1 application-test.properties configuration

```properties
# H2 database
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop

jwt.secret=dMoDvbJNhCYnJrxwxy2HXh2DeaoIU5RUl6Efrzq6aCs=

logging.level.org.springframework.security=ERROR
```
### 3. Database
You do not need to create a data table for, Hibernate itself will create tables from entities.

But certainly if something goes wrong 🙂
``` sql
create table items
(
id    binary(16)   not null
primary key,
owner binary(16)   not null,
name  varchar(255) not null,
constraint items_ibfk_1
foreign key (owner) references users (id)
);

create index owner
on items (owner);
``` 
``` sql
create table users
(
    id       binary(16)   not null
        primary key,
    username varchar(255) not null,
    password varchar(255) not null
);
```
### 4. Build and launch
``` 
./mvnw clean install
./mvnw spring-boot:run
```

