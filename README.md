# Load Balancing Algorithms

This project demonstrates several classic and modern load balancing algorithms, implemented in Java 21 using Spring Boot 3.x. It includes a simple UI to test and compare the algorithms in action.

## Algorithms Implemented

1. **Round Robin**
   - Distributes requests to servers in a circular order.
   - Supports Weighted Round Robin for assigning more requests to powerful servers.
2. **Least Connections**
   - Routes requests to the server with the fewest active connections.
   - Supports server weights.
3. **Hashing**
   - Uses a hash of a key (e.g., client IP or URL) to consistently route requests to the same server.
4. **Consistent Hashing**
   - Minimizes re-routing when servers are added/removed.
   - Implements Ring Hashing (and mentions Maglev).

## Features
- Add/remove servers dynamically.
- Simulate requests with custom keys.
- Select and test different load balancing algorithms.
- Visualize which server handles each request.

## Tech Stack
- Java 21
- Spring Boot 3.x
- Thymeleaf (for UI) or REST API with Swagger UI

## Getting Started
1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd load-balancing-algorithms
   ```
2. **Build and run the project**
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Access the UI**
   - Open [http://localhost:8080](http://localhost:8080) in your browser.

## License
MIT 