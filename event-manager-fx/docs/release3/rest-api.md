# Event manager API

The server is a Spring Boot application, default address <http://localhost:8080>.
This URI is not hardcoded, as it can be changed in the client api wrappers   by the classes calling it, but for this release the default address will be used. 

* Dates are sent as ISO local date-time, e.g. `"2026-12-24T18:00:00"`
* DTOs are used for data transfer over the API, refer to the [DTOs](#dtos) section of this document.
* Endpoints marked *auth required* need the header `Authorization: {username}`
    * the username is currently used directly as the token, it is not validated


# Event API

## Base endpoint

<http://localhost:8080/api/events>

Methods:

* GET - retrieves a list of all events from the server, sorted by time
    * URI: host:port/api/events
      <http://localhost:8080/api/events>
    * parameters: none
    * available: springboot
    * returns
        * Content-Type: application/json
        * JSON with the list of events (empty list if there are none)

```json
[
  {
    "eventId":"event-1",
    "title":"Board game night",
    "place":"Room 101",
    "time":"2026-12-24T18:00:00"
  },
  {
    "eventId":"event-2",
    "title":"Quiz",
    "place":"Auditorium",
    "time":"2026-12-31T20:00:00"
  }
]
```

* POST - creates a new event, the creator becomes admin of the event (*auth required*)
    * URI: host:port/api/events
    * parameters:
        * header - Authorization: {username}
        * body - application/json; charset=UTF-8
        * event content

```json
{
  "eventId":"event-1",
  "title":"Board game night",
  "place":"Room 101",
  "time":"2026-12-24T18:00:00"
}
```

* available: springboot
* returns
    * `201 Created`
        * Content-Type: application/json
        * JSON with the created event
    * `409 Conflict` if an event with that eventId already exists

```json
{
  "eventId":"event-1",
  "title":"Board game night",
  "place":"Room 101",
  "time":"2026-12-24T18:00:00"
}
```

## event

<http://localhost:8080/api/events/{id}>

Methods:

* GET - retrieves the specified event from the server
    * URI: host:port/api/events/{id}
      <http://localhost:8080/api/events/event-1>
    * parameters: none
    * available: springboot
    * returns
        * `200 OK`
            * Content-Type: application/json
            * JSON with the event content
        * `404 Not Found` if there is no event with that id

```json
{
  "eventId":"event-1",
  "title":"Board game night",
  "place":"Room 101",
  "time":"2026-12-24T18:00:00"
}
```

* PUT - updates the event with the id {id}
    * URI: host:port/api/events/{id}
    * parameters:
        * body - application/json; charset=UTF-8
        * event content

```json
{
  "eventId":"event-1",
  "title":"Board game night (moved)",
  "place":"Room 202",
  "time":"2026-12-24T19:00:00"
}
```

* available: springboot
* returns
    * `200 OK`
        * Content-Type: application/json
        * JSON with the updated event
    * `404 Not Found` if there is no event with that id

* DELETE - deletes the event with the id {id}, and removes it from all users attending it
    * URI: host:port/api/events/{id}
    * parameters: none
    * available: springboot
    * returns
        * `204 No Content` on success
        * `404 Not Found` if there is no event with that id

# User API

## login

<http://localhost:8080/api/users/login>

Methods:

* POST - checks the username and password
    * URI: host:port/api/users/login
    * parameters:
        * body - application/json; charset=UTF-8
        * username and password

```json
{
  "username":"alice",
  "password":"secret"
}
```

* available: springboot
* returns
    * Content-Type: application/json
    * JSON with the username and whether the login succeeded
    * a failed login also returns `200 OK`, with `"success":false`

```json
{
  "id":"alice",
  "success":true
}
```

## register

<http://localhost:8080/api/users/register>

Methods:

* POST - registers a new user
    * URI: host:port/api/users/register
    * parameters:
        * body - application/json; charset=UTF-8
        * username and password

```json
{
  "username":"alice",
  "password":"secret"
}
```

* available: springboot
* returns
    * Content-Type: application/json
    * JSON with the username and whether the registration succeeded
    * a rejected registration (e.g. the username is taken) returns `200 OK`, with `"success":false`

```json
{
  "id":"alice",
  "success":true
}
```

## me

<http://localhost:8080/api/users/me>

Methods:

* DELETE - deletes the user, together with all events the user has made (*auth required*)
    * URI: host:port/api/users/me
    * parameters:
        * header - Authorization: {username}
    * available: springboot
    * returns
        * `204 No Content` on success
        * `404 Not Found` if the user could not be deleted

## me/events

<http://localhost:8080/api/users/me/events>

Methods:

* GET - retrieves the events the user is attending (*auth required*)
    * URI: host:port/api/users/me/events
      <http://localhost:8080/api/users/me/events>
    * parameters:
        * header - Authorization: {username}
    * available: springboot
    * returns
        * `200 OK`
            * Content-Type: application/json
            * JSON with a map of event id to whether the user is admin of that event
        * `401 Unauthorized` if the user is not known

```json
{
  "events":{
    "event-1":true,
    "event-2":false
  }
}
```

## me/events/{eventId}

<http://localhost:8080/api/users/me/events/{eventId}>

Methods:

* PUT - opts the user in to the event (*auth required*)
    * URI: host:port/api/users/me/events/{eventId}
    * parameters:
        * header - Authorization: {username}
    * available: springboot
    * returns
        * `204 No Content` on success
        * `404 Not Found` if the event does not exist
        * `409 Conflict` if the user is already attending the event

* DELETE - opts the user out of the event (*auth required*)
    * URI: host:port/api/users/me/events/{eventId}
    * parameters:
        * header - Authorization: {username}
    * available: springboot
    * returns
        * `204 No Content` on success
        * `404 Not Found` if the event does not exist
        * `409 Conflict` if the user was not attending the event

# DTOs

The request and response bodies are records from the `api-contract` module (package `dto`).

## EventDto

Used in all the event endpoints.

```json
{
  "eventId":"event-1",
  "title":"Board game night",
  "place":"Room 101",
  "time":"2026-12-24T18:00:00"
}
```

* eventId - string, the unique id of the event, must not be blank
* title - string, the title of the event, must not be blank
* place - string, where the event takes place, must not be blank
* time - string, when the event starts, ISO local date-time, must not be null
* an event that breaks these rules cannot be created on the server, and the request fails

## LoginRegisterRequestDto

Used as the body of login and register.

```json
{
  "username":"alice",
  "password":"secret"
}
```

* username - string, the username of the user
* password - string, the password of the user

## LoginRegisterResponseDto

Returned by login and register.

```json
{
  "id":"alice",
  "success":true
}
```

* id - string, the username that was sent in the request
* success - boolean, true if the login or registration succeeded, false otherwise

## UserGetEventsResponseDto

Returned by `GET /api/users/me/events`.

```json
{
  "events":{
    "event-1":true,
    "event-2":false
  }
}
```

* events - map from event id to a boolean
    * true if the user is admin of the event (the user created it)
    * false if the user is only attending the event
