# Event Manager

[Open in Eclipse Che](https://che.iaas.ntnu.no/#https://git.ntnu.no/IT1901-2026-groups/gr2607?new)

Event Manager is a client-server application developed as part of the NTNU course IT1901.

The project is located in [`event-manager-fx/`](event-manager-fx) and consists of the following modules:

- [`core`](event-manager-fx/core) – Core application logic and domain models.
- [`persistence`](event-manager-fx/persistence) – Data persistence.
- [`ui`](event-manager-fx/ui) – JavaFX desktop client.
- [`api-contract`](event-manager-fx/api-contract) – Shared API contracts.
- [`springboot/restserver`](event-manager-fx/springboot/restserver) – Spring Boot REST API.
- [`jacoco-aggregator`](event-manager-fx/jacoco-aggregator) – Aggregates JaCoCo test coverage reports.

## Prerequisites

To build and run the project from source, you need:

- Java 17 or later.
- Maven (or the included Maven wrapper).
- JavaFX-compatible environment for running the desktop client.

All Maven commands below assume that you are working from `event-manager-fx/`, unless otherwise specified. The included Maven wrapper can be used instead of a system Maven installation.

## Running from source

The application consists of two independently running components: the REST server and the JavaFX client. Both must be running for the application to function.

### 1. Build the project

From `event-manager-fx/`, run:

```bash
./mvnw clean install
```

This builds the modules and makes their dependencies available locally.

Other useful Maven commands:

```bash
./mvnw compile   # Compile the project
./mvnw test      # Run tests
./mvnw verify    # Run quality checks and verification
```

### 2. Start the REST server

Open a terminal and navigate to the REST server module:

```bash
cd springboot/restserver
../../mvnw spring-boot:run
```

The server uses the configuration in `springboot/restserver/src/main/resources/application.properties`.

Keep this terminal running.

### 3. Start the desktop client

Open a second terminal:

```bash
cd ui
../../mvnw javafx:run
```

The JavaFX client will start and communicate with the REST server.

Both components must be running simultaneously.

## Packaging the desktop client

The desktop client is packaged using `jlink` and `jpackage`.

- `jlink` creates a custom Java runtime containing the required modules.
- `jpackage` uses this runtime to generate a distributable application package.

All commands below assume that you are working from `event-manager-fx/`.

### 1. Create the runtime image

Run the following command:

```bash
./mvnw -pl ui javafx:jlink
```

This generates the custom runtime image in `ui/target/image/`.

### 2. Create the application package

Run the appropriate command for your target operating system.

**Linux**

```bash
jpackage \
    --type app-image \
    --name EventManager \
    --app-version 1.0.0 \
    --runtime-image ui/target/image \
    --module eventmanager.ui/ui.EventManagerApp \
    --dest dist
```

**Windows (PowerShell)**

```powershell
jpackage `
    --type exe `
    --name EventManager `
    --app-version 1.0.0 `
    --runtime-image ui/target/image `
    --module eventmanager.ui/ui.EventManagerApp `
    --dest dist
```

**macOS**

```bash
jpackage \
    --type dmg \
    --name EventManager \
    --app-version 1.0.0 \
    --runtime-image ui/target/image \
    --module eventmanager.ui/ui.EventManagerApp \
    --dest dist
```

The generated package is placed in the `dist/` directory.

**Important:** Packaging must be performed on the target operating system. The runtime image must also be generated on that operating system; a Linux runtime image cannot be reused to create a Windows or macOS package.
