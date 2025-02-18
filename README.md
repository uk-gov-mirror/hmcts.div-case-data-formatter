# Divorce Case Formatter Service [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This is the Divorce Case Formatter Service which handles the different data conversion between different Divorce Components.
For example this will convert the Front End Session Data to CCD data format and vice versa.

## Setup

**Prerequisites**

- [JDK 11](https://openjdk.java.net)
- [Docker](https://www.docker.com)

**Building**

The project uses [Gradle](https://gradle.org) as a build tool but you don't have to install it locally since there is a
`./gradlew` wrapper script.

To build project please execute the following command:

```bash
    ./gradlew build
```

**Running**

First you need to create distribution by executing following command:

```bash
    ./gradlew installDist
```

When the distribution has been created in `build/install/div-case-formatter-service` directory,
you can run the application by executing following command:

```bash
    docker-compose up
```

As a result the following container(s) will get created and started:
 - long living container for API application exposing port `4011`

## Testing

To run all unit tests please execute following command:

```bash
    ./gradlew test
```


**Mutation tests**

To run all mutation tests execute the following command:

```
./gradlew pitest

```

**Integration tests**

To run all integration tests locally:

* Make a copy of `src/main/resources/example-application-aat.yml` as `src/main/resources/application-aat.yml`
* Make a copy of `src/integrationTest/resources/example-application-local.properties` as `src/integrationTest/resources/application-local.properties`
* Replace the `replace_me` secrets in the _newly created_ files. You can get the values from SCM and Azure secrets key vault (the new files are in .gitignore and should ***not*** be committed to git)
* Start the app with AAT config:
  * Using gradle: `./gradlew clean bootRunAat`
  * Using Intellij: edit Run Configuration and set Environment variables to `http_proxy=http://proxyout.reform.hmcts.net:8080;SPRING_PROFILES_ACTIVE=aat`
* Run the tests with AAT config using `./gradlew clean functional`

## Developing

**Coding style tests**

To run all checks (including unit tests) please execute following command:

```bash
    ./gradlew check
```

**Versioning**

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

**Standard API**

We follow [RESTful API standards](https://hmcts.github.io/restful-api-standards/).

**API documentation**

API documentation is provided with Swagger:
 - `http://localhost:4011/swagger-ui.html` - UI to interact with the API resources

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

