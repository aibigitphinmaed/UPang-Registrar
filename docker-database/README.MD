Running and Building
```
docker compose config
docker compose up --build
```
To reset the database/volume just in case you mess it up
```
docker compose down -v
```
To access postgresdb(postgres database) via docker
```
docker exec -it postgres-container psql -U postgres -d postgresdb
```
To remove existing Container if ever you encounter
error: The container name "/postgres-container" is already in use by container
```
docker stop upangregistrar-container
docker rm upangregistrar-container
```
