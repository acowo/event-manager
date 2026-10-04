# Code quality

## Architecture

The architecture of the project should keep the persistence, core and UI modules separate. Controllers should not implement domain logic, core should not make changes to UI and so on. This will in turn make the later stages of the project, as well as refactoring/rewriting parts of the code easier.

## Code readability/style

The code should have clear and consise variable/method naming, and follow standard Java convensions as outlined in the code style paragraph of [CONTRIBUTING.md](/CONTRIBUTING.md#code-style)

## JavaDoc

JavaDoc should be written for methods/objects that other team members will use, so the integration is as pain-free as possible with regards to throws, returns and inputs.
