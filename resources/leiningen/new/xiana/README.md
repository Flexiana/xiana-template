# {{name}}

FIXME: description

## Usage

### Start dockerized PostgreSQL

    docker-compose up -d
    
### Log into psql console

    psql -U postgres -p 5433 -h localhost

### Build frontend and run the backend

    lein release

    lein run

### Try {{name}}

    curl http://localhost:3000/

### Open re-frame app

    open http://localhost:3000/re-frame
    