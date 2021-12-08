# {{name}}

FIXME: description

## Usage

### Start dockerized PostgreSQL

    docker-compose up -d

### Log into psql console

    psql -U postgres -p 5433 -h localhost

### Prepare node-dependencies

    lein shadow npm-deps

### Start development

Jack in a repl, execute
```clojure
(user/start-dev-system)
```
It will start up the shadow watch and the backend. It can be used to restart the whole application too.

### Build frontend and run the backend

    lein release

    lein run

### Try {{name}}

    curl http://localhost:3000/

### Open re-frame app

    open http://localhost:3000/re-frame
    