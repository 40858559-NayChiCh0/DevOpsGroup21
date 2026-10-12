FROM mysql:8.0
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=world
COPY world-db/world.sql /docker-entrypoint-initdb.d/world.sql
