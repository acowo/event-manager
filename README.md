
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
