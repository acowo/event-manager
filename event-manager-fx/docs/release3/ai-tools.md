# AI Usage

## Platforms and models

AI tools such as Claude Sonnet 5 and OpenAI GPT-5.6 Luna and their respective web clients, as well as IDE-integrated tools like GitHub Copilot, have been used in this project.

## Changes in AI usage

Compared to Release 2, our use of AI tools has expanded beyond documentation and comments. AI has increasingly been used as an aid during development, particularly when troubleshooting configuration issues, understanding unfamiliar tooling/architecture and improving the development workflow. AI has also been implemented more in creating tests.

## Example usage

One concrete example was the attempted use of GitHub Copilot to address Checkstyle violations. The experience proved extremely inefficient due to this process using a lot of tokens (going through the entire codebase, a lot of small changes). Thus, we experienced that AI usage is not always an especially efficient way to solve these problems. Mechanical formatting and straightforward style corrections are often better handled by dedicated tools or manual changes.

AI has also been useful for investigating build configuration, CI pipeline issues and understanding the implications of different development practices. LLMs have also been used to aid in understanding how to build a REST API architecture in Java. In these situations, it has functioned as a discussion partner and troubleshooting aid, rather than a code generator.

## Summary

Overall, our approach remains focused on maintaining an understanding of the code and critically evaluating AI-generated suggestions. However, our experience in Release 3 has given us a broader understanding of where AI can be useful, as well as its limitations in terms of reliability, resource consumption and the need for human verification.
