# File storage metaphor and format

## Metaphor

The file storage in this project uses an "implicit saving" (autosave) method upon changes to the file data, be it user or event data.
This is done because having separate "save/load" functionality that the user can interact with is unlike actual (physical) calendars and other event managers that we used as references for this project, mostly implicitly, through interacting with calendars of our own (various calendar apps, for example).
The "save/load" functionality tends to be associated with text documents, image manipulation or other kinds of creative work. This is not befitting of a calendar, where it is usually expected that a change will be reflected in the calendar immediately.
Thus, having "save/load" functionality would make the application behave less like the system it is supposed to represent.

## Format

The event data, saved in [eventdata.json](../../data/eventdata.json), is formatted as a dictionary with the event title as the key, and the event object as the value:

```json
{key<event title>:{"title":"<event title>","place":"<event place>","time":"<ISO 8601 standard date and time string>"}}
```

This can, and probably will be improved upon in further releases, changing (perhaps using hashing) the key so multiple events can have the same name.

User data [(userdata.json)](../../data/userdata.json) is stored in much the same way, with the username as the key, and the user object stored as the value.

```json
{"<username>":{"eventIdList":[],"username":"<username>","password":"<password>"}}
```

Passwords are stored in plaintext. This can, and again probably will, be remedied in a further release, likely by hashing.
