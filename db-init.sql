-- Used by docker-compose.

CREATE USER smesp WITH LOGIN PASSWORD 'smesp';
CREATE DATABASE smesp;
GRANT ALL PRIVILEGES ON DATABASE smesp TO smesp;
