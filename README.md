# Assessment Fullstack Backend

This application is a Spring Boot 3 backend microservice to deal with Assessment Fullstack Frontend requests.

## Prerequisites

- OpenJDK 21 (LTS)

## Run Application in Terminal

### Checkout Project from GitHub

```
cd <workspace>
git clone git@github.com:tom-reno/assessment-fullstack-backend.git
```

### Configure Application

#### CSV configuration

CSV files are the default data source. Define the directory in application.yml as follows:

```
app.csv.person.directory: file:<directory>
```

A `sample-input.csv` is located in the directory `<project-root>/data/csv/person`. Therefore, set `project-root`
accordingly.

#### Database configuration

To use the database instead, configure the following property in application.yaml:

```
app.database.enabled: true
```

### Install Dependencies

```
cd assessment-fullstack-backend
./mvnw install
```

### Start Application

```
./mvnw spring-boot:run
```
