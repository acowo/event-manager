# Team member contributions

## Attribution disclaimer

The git commits will be slightly misattributed when running for example `git log`, as some commits were made under multiple names or email addresses. In particular, Sander's contributions are split between `sandergao5439@gmail.com` and `sandergk@stud.ntnu.no`; Angelica's between `angeliys@stud.ntnu.no` and `angelica@skarsaune.net`; Oskar's between `oskarehe@stud.ntnu.no` and `oskar@oskheb.no`; and Sana's appear under `sanak@stud.ntnu.no` and different display names

## Don Vo <donv@stud.ntnu.no>

Don Vo contributed primarily to the user management system and the login interface. He implemented the `User` and `UserSession` classes, including their supporting methods, and developed the `UserManager` responsible for user-related operations. He also worked on resolving architectural issues between the core and persistence modules, including a circular dependency. In addition, he contributed to the visual design of the login interface and added test data for the user system. His documentation contributions included sequence and module diagrams, updates to existing diagrams and Javadoc for user- and event-related classes. He also worked on the Maven configuration to ensure that test coverage reporting functioned correctly for the UI module.

## Angelica Yen Skarsaune <angeliys@stud.ntnu.no>

Angelica Yen Skarsaune contributed to the user system and its integration with the application. Her work included implementing and integrating functionality related to user accounts, password checking and the login page. She also contributed to testing the user persistence layer and improving test coverage, as well as establishing the TestFX setup for UI testing. In addition, she participated in the development and integration of the calendar functionality and contributed to the project's build and test infrastructure, including the JaCoCo aggregation setup.

## Oliver Naper <olivena@stud.ntnu.no>

Oliver Naper contributed to several areas of the project, with a particular focus on project structure, build configuration, documentation and development practices. He worked on establishing the modular Maven setup, ensuring consistent module naming and configuring the project to launch correctly through the JavaFX client. He also contributed to the persistence layer, including improvements to event file handling, and worked on integrating JaCoCo for test coverage reporting. A substantial part of his work involved project documentation, including the README, CONTRIBUTING guidelines, architecture documentation, code quality standards, AI usage documentation and descriptions of the group's workflow. Additionally, he helped establish more consistent development practices by introducing GitHub issue and pull request templates, configuring Google Checkstyle and addressing SpotBugs findings. He also contributed to bug fixes and test-related changes throughout the release.

## Oskar Emil Hebnes <oskarehe@stud.ntnu.no>

Oskar Emil Hebnes contributed to the project's initial modular structure and the development of its user interface. His implementation work included setting up the login page and its associated controller, integrating login functionality with the core module and improving user feedback through input validation. He also contributed to repository maintenance, including cleaning up project files and correcting the Git ignore configuration. Furthermore, he participated in testing the login functionality using TestFX and contributed documentation, including updates to the README and Javadoc for persistence-related classes and methods.

## Sana Karami <sanak@stud.ntnu.no>

Sana Karami contributed primarily to the event domain and persistence layers, as well as their associated tests. She implemented the initial event file handling functionality and added validation to the `Event` constructor. She also helped establish the `EventRepository` interface and its implementation in the persistence layer, alongside the `EventManager` responsible for event operations. A significant part of her work involved improving test coverage for the event and user systems, including tests for `Event`, `EventFileManager`, `UserFileManager` and `LocalDateTimeAdapter`. She also contributed to making persistence tests more reliable by isolating them and removing an unreliable test. In addition, she co-authored several changes addressing SpotBugs findings in the persistence module.

## Sander Kjeøy <sandergk@stud.ntnu.no>

Sander Kjeøy contributed extensively to the project's persistence and core functionality. He implemented user persistence using JSON files, including functionality for storing and retrieving user information and validating passwords. He also worked on the event persistence system, introducing event identifiers based on UUIDs rather than event titles. In the core layer, he contributed to the repository and manager implementations, including defensive copying of users, events and repositories to prevent unintended modifications to internal state. He also worked on testing these implementations and improving error handling. In addition, he contributed to the project's static analysis setup by configuring SpotBugs across multiple modules and addressing the resulting findings.
