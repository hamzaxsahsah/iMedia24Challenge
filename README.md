# iMedia24 Shop API Docker Guide

This document provides instructions for building and running the iMedia24 Shop API application using Docker.

## Prerequisites

- [Docker](https://www.docker.com/get-started) installed on your system
- Git (to clone the repository)

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd imedia24-shop-api
```

### Build the Docker Image

You can build the Docker image using the provided Dockerfile:

```bash
docker build -t imedia24-shop-api:latest .
```

This command builds the Docker image with the tag `imedia24-shop-api:latest`.

### Run the Docker Container

Once the image is built, you can run the container:

```bash
docker run -p 8080:8080 imedia24-shop-api:latest
```

This command runs the container and maps port 8080 of the container to port 8080 on your host machine.

## API Endpoints

The following endpoints are available:

- **GET /product/{sku}**: Get product details by SKU
- **GET /products?skus=123,456,789**: Get multiple products by SKUs
- **POST /product**: Create a new product
- **PATCH /product/{sku}**: Update product details

## Environment Variables

You can customize the application by setting environment variables when running the container:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb \
  -e SPRING_DATASOURCE_USERNAME=sa \
  -e SPRING_DATASOURCE_PASSWORD=password \
  imedia24-shop-api:latest
```

## Using Docker Compose

For convenience, you can use Docker Compose to run the application. Create a `docker-compose.yml` file:

```yaml
version: '3'
services:
  shop-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
      - SPRING_DATASOURCE_USERNAME=sa
      - SPRING_DATASOURCE_PASSWORD=password
```

Then run:

```bash
docker-compose up
```

## API Documentation

The API documentation is available via Swagger UI at:

```
http://localhost:8080/swagger-ui/
```

## Testing the API

You can use tools like curl or Postman to test the API:

### Get Product by SKU

```bash
curl -X GET http://localhost:8080/product/123
```

### Get Multiple Products by SKUs

```bash
curl -X GET http://localhost:8080/products?skus=123,456,789
```

### Create a New Product

```bash
curl -X POST http://localhost:8080/product \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "123",
    "name": "Sample Product",
    "description": "This is a sample product",
    "price": 19.99,
    "stock": 50
  }'
```

### Update a Product

```bash
curl -X PATCH http://localhost:8080/product/123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product Name",
    "price": 24.99
  }'
```

## Troubleshooting

If you encounter any issues:

1. Check if the container is running:
   ```bash
   docker ps
   ```

2. Check the container logs:
   ```bash
   docker logs <container-id>
   ```

3. Make sure the port mapping is correct and not conflicting with other services

## Building for Production

For production deployments, consider:

1. Using a proper database instead of H2 in-memory database
2. Configuring appropriate resource limits for the container
3. Setting up proper logging and monitoring
4. Implementing healthchecks for container orchestration

## License

This project is licensed under the terms specified in the project repository.