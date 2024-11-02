# Test containers

[![codecov](https://codecov.io/gh/qReolq/test-container/graph/badge.svg?token=9OYZMRI6M6)](https://codecov.io/gh/qReolq/test-container)

Testcontainers tests of Spring Boot application.

This application is a simple post service, that allows to create, get and delete
posts.
### Environments

You need to provide next variables in `.env` file.

* `HOST` - host with port of Postgres instance
* `POSTGRES_DB` - name of database
* `POSTGRES_USERNAME` - username of user
* `POSTGRES_PASSWORD` - password of user

### Run

To run this application you need to run `docker compose up`.
