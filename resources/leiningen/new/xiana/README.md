# {{name}}

FIXME: description

## Usage

### Start dockerized PostgreSQL

```shell
docker-compose up -d
```

### Log into psql console

```shell
psql -U postgres -p 5433 -h localhost
```

### Prepare node-dependencies

```shell
lein shadow npm-deps
```

### Start development

Jack in a repl, execute

```clojure
(user/start-dev-system)
```

It will start up the shadow watch and the backend. It can be used to restart the whole application too.

### Build frontend and run the backend

```shell
lein release && lein run
```

### Try {{name}}

```shell
curl http://localhost:3000/
```

### Open re-frame app

open http://localhost:3000/re-frame

### Framework's documentation

[Xiana readme](https://github.com/Flexiana/framework#readme)