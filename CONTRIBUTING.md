# Contributing to Event Manager

We use GitHub Issues, feature branches, pull requests, and automated tests.

Keep responsibilities separated:

* `core` contains domain logic and models.
* `persistence` handles reading and writing data.
* `ui` contains JavaFX-specific code.
* UI and domain classes should not directly handle file I/O.

## Development

The project uses Java, Maven, and JavaFX. It is intended to be developed and assessed using Eclipse Che.

Run from `event-manager-fx/`:

```bash
mvn clean
mvn test
```

Make sure tests pass before submitting changes.

## Git Workflow

Create a branch for each issue or feature:

```text
feature/event-creation
feature/user-login
bugfix/event-loading
```

Keep branches focused on one issue where possible.

Use clear commit messages:

```text
Add Event domain model
Implement file-based event persistence
Add event creation controller
Fix event loading
```

Create a pull request when the work is ready for review. Reference the relevant GitHub issue and keep pull requests reasonably small. 
Request review from other group members. Do not merge your own PRs to main.

Do not merge unfinished work into `main`.

## Testing

Add tests for new or changed functionality where appropriate.

Tests belong in the corresponding module:

```text
src/test/java/
```

Run:

```bash
mvn test
```

## Code Style

Follow standard Java conventions:

* Classes and interfaces: `PascalCase`
* Methods and variables: `camelCase`
* Constants: `UPPER_SNAKE_CASE`
* Packages: lowercase
* Use meaningful names and keep methods focused.
* Prefer simple solutions over unnecessary abstractions.

The project uses **Checkstyle** and **SpotBugs**. Fix warnings rather than disabling them unless there is a good reason.

## Data and Generated Files

Keep application data separate from source code. Do not put data files in `src/main/java` or `src/test/java`.

Do not commit generated Maven files such as:

```text
target/
*.class
```

## Documentation

Documentation belongs in:

```text
docs/
└── release1/
```

Update relevant documentation when making changes that affect the architecture, functionality, or setup.
