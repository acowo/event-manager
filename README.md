
# Event Manager

[open in Eclipse Che](https://che.iaas.ntnu.no/#https://git.ntnu.no/IT1901-2026-groups/gr2607?new)

This repository contains the project Event Manager, for class IT1901. The project itself is contained in [`event-manager-fx`](event-manager-fx).
Modules are [`event-manager-fx/core`](event-manager-fx/core), [`event-manager-fx/persistence`](event-manager-fx/persistence) and [`event-manager-fx/ui`](event-manager-fx/ui)

The project can be tried out by cd-ing into the corresponding folder and using `mvn`:

- `cd event-manager-fx`
- run `mvn clean install` to allow local dependencies to be available
- compile with `mvn compile`
- test with `mvn test`
- run quality checks with `mvn verify`
- `cd ui`, then `mvn javafx:run`

Test coverage is measured using JaCoCo.
To generate the test coverage report, run the following command from `event-manager-fx`:

`mvn clean verify`

The JaCoCo report is then generated at:
`jacoco-aggregator/target/site/jacoco-aggregate/index.html`

Open index.html in a web browser to view the project's test coverage.
