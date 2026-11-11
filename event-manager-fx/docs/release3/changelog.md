# Release 2

## Features
- Users can now be deleted.
- A dialog pane shows up to make sure user doesn't accidentally delete an event or itself.
- Multiple events can now have the same name.
- The creator of an event will be set as administrator
  - Only the administrator of the event is able to delete the event
  - The administrator is automattically opted in to the event, and cannot opt out.
- User and event data is now stored through Spring Boot
