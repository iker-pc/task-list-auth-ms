# Task List auth microservice

This repository holds the backend code used by the [task list web application] to manage users' authentication and authorization.

In order to request data, by using the controllers exposed by the [task list manager microservice], an authorization header, containing a valid jwt token,  must be provided. The auth microservice checks the authenticity of that token by using the [auth0] external service. If it is valid, user's data can be requested.

[task list web application]: https://github.com/iker-pc/task-list-webapp
[task list manager microservice]: https://github.com/iker-pc/task-list-manager-ms
[auth0]: https://auth0.com/

  
