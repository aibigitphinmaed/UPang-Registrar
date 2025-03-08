FROM postgres:latest

# Copy initialization SQL script
COPY init.sql /docker-entrypoint-initdb.d/

# Expose PostgreSQL port
EXPOSE 5432

