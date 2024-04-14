# About Soundmate

Soundmate is a native IOS/Android application to find friends based on your Spotify Music Library. The frontend code can
be found in another repository: [soundmate-frontend](https://github.com/finja-scheltens/soundmate-fe)

The backend application in this repository offers an API for account & profile creation and contains the matching
algorithm for Soundmate. All endpoints are secured with Spring Security and JWTs. Data is stored in a postgres database,
which is set up by liquibase.

Soundmate was initially developed as part of a university course at [HTW Berlin](https://www.htw-berlin.de/) taught by [Prof. Dr. Regina Frieß](https://www.htw-berlin.de/hochschule/personen/person/?eid=12381), and then further developed under the supervision of [Prof. Dr. Gefei Zhang](https://www.htw-berlin.de/hochschule/personen/person/?eid=10755).


# Local Setup

## Database

This backend project requires a postgres server running locally on port 5432 with an already existing database

* Name: `soundmate`
* Username: `postgres`
* Password: whatever you wish

Please make sure this is set up before starting your application. Your used password needs to be set as environment
variable `PG_PASSWORD` when starting the application. In case you want to use completely different credentials or a
different port/server-host, you can modify those in the application.yml local properties, but do not push these changes.

## Run Config

The following environment variables are required to start this application:

* `PG_PASSWORD`as mentioned above
* `JWT_SECRET` a random string, used to generate JWTs
* `SPOTIFY_CLIENT_SECRET` the secret for the soundmate client application, as provided by Spotify

A sample run configuration for IntelliJ IDEA can be found [here](.run/Soundmate.run.xml).

# Conventions

## Code Conventions

All of these are not mandatory and often up for discussion, due to personal preferences.

### SonarLint

IntelliJ has a SonarLint plugin which can be configured to review all your changes before a commit.
Linters can be used to detect code smells and vulnerabilities, while also enforcing specific code conventions
and style choices. If SonarLint is used, all shown issues should at least be reviewed before a commit, and
**all** Blocker and Critical issues should be resolved.

### Other general guidelines

* Entities should not be handed around the application too much. In case you want to
  return an entity from a controller, please use [Mapstruct](https://mapstruct.org/) to create a
  [Data Transfer Object (DTO)](https://www.baeldung.com/java-dto-pattern) and replace all uses of the entity class
  with this DTO.
* Use Lombok to avoid boilerplate code. In most cases, there is no need to write your own getters,
  setters or constructors.
* Please re-format your code, run tests (if we add any) and check any TODOs you've added before you commit.
  IntelliJ can be configured to do this before you commit.

# Deployment

In order to deploy the java application, a private virtual debian-server was used. As can be seen in the
[Dockerfile](Dockerfile) and [docker-compose](docker-compose.yml) file the app was built and deployed in docker.
All secret environment variables were provided as docker secrets and the postgres data is stored in a mounted volume.

Redeployment is currently done by hand, by executing:

* `mvn clean install -DskipTests`
* `docker compose build`
* `docker compose up`
