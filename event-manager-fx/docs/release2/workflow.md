# Workflow

*Much of this information is already covered (implicitly and explicitly) in [`CONTRIBUTING.md`](/CONTRIBUTING.md)*

Our development workflow for release 2 primarily uses GitHub Issues, branches, pull requests and pair programming (new from release 1). The goal is to give every team member a clear picture of what is being worked on by whom, and what they are doing, which contributes to giving everyone concrete goals/tasks to work towards.

## Issues

All coding tasks are created as GitHub Issues before work starts. Issues are used to keep track of what needs to be implemented, fixed or documented.

Issues are connected to the Release 2 milestone so that we can keep track of the progress towards the release.

## Branches

Each coding task is worked on in its own branch. The branch is based on `main` and is named according to the task it is related to.

For example:

```text
feature/event-persistence
feature/event-tests
docs/release2-workflow
```

When a task is finished, a pull request is created from the issue branch into `main`.

## Pair programming

One person is the driver and writes the code, while the other person reviews the code as it is being written, and helps with the implementation. We switch roles during the work so that both people participate in the implementation.

Commits made as part of pair programming include a `Co-authored-by:` footer for the other person who contributed to the commit.

## Pull requests

Completed work is submitted through a pull request. Before merging, another group member reviews the changes.

The reviewer checks things such as:

* whether the issue has been solved
* whether the code fits the codebase's architecture
* whether appropriate tests have been added
* whether the code follows the project's existing conventions
* whether the changes introduce any obvious problems

If changes are needed, they are suggested by the reviewer, made on the same branch, then the pull request is updated. When the review is complete, the pull request can be merged into `main`.

## Release workflow

Work towards a release is organized through a GitHub milestone.

The general workflow is:

```text
Issue
  ↓
Create branch
  ↓
Pair programming (if required)
  ↓
Commit changes (repeat if needed for structure/clarity)
  ↓
Run tests and quality checks
  ↓
Code review
  ↓
Create pull request
  ↓
Merge into main
```
