docker run --rm --name pg-docker ^
-e POSTGRES_PASSWORD=postgres ^
-e POSTGRES_DB=demoDB ^
-p 5430:5432 ^
postgres:latest