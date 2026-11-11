
# Event Manager

[open in Eclipse Che](https://che.iaas.ntnu.no/#https://git.ntnu.no/IT1901-2026-groups/gr2607?new)

This repository contains the project Event Manager, for class IT1901. The project itself is contained in [`event-manager-fx`](event-manager-fx).
Modules are [`event-manager-fx/core`](event-manager-fx/core), [`event-manager-fx/persistence`](event-manager-fx/persistence), [`event-manager-fx/ui`](event-manager-fx/ui), [`event-manager-fx/springboot/restserver`](event-manager-fx/springboot/restserver) and [`event-manager-fx/api-contract`](event-manager-fx/api-contract). There is also the helper module [`event-manager-fx/jacoco-aggregator`](event-manager-fx/jacoco-aggregator) for JaCoCo report generation.

Prerequisites before running the project:
- cd into [`event-manager-fx`](event-manager-fx)
- run `mvn clean install` to allow local dependencies to be available
- compile with `mvn compile`
- test with `mvn test`
- run quality checks with `mvn verify`

The project is run by firstly opening a terminal, CDing into [`event-manager-fx/springboot/restserver`](event-manager-fx/springboot/restserver) and running `mvn spring-boot:run` this starts the server, then opening another terminal, CDing into [`event-manager-fx/ui`](event-manager-fx/ui) and running the command `mvn javafx:run`, this starts the UI. Both terminals must be run separately as this is a client-server based application.



Test coverage is measured using JaCoCo.
To generate the test coverage report, run the following command from `event-manager-fx`:

`mvn clean verify`

The JaCoCo report is then generated at:
`jacoco-aggregator/target/site/jacoco-aggregate/index.html`

Open index.html in a web browser to view the project's test coverage.
