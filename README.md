# Load Balancing Algorithms

This project demonstrates several classic and modern load balancing algorithms, implemented in Java 21 using Spring Boot 3.x. It features a **modern, interactive UI** for testing, visualizing, and comparing the algorithms in action.

## Algorithms Implemented

1. **Round Robin**
   - Distributes requests to servers in a circular order.
   - Supports Weighted Round Robin for assigning more requests to powerful servers.
2. **Least Connections**
   - Routes requests to the server with the fewest active connections.
   - Supports server weights.
3. **Hashing**
   - Uses a hash of a key (e.g., client IP or URL) to consistently route requests to the same server.
4. **Consistent Hashing (Ring Hashing)**
   - Minimizes re-routing when servers are added/removed.
   - Implements classic ring-based consistent hashing with virtual nodes.
5. **Maglev Hashing**
   - Implements Google's Maglev consistent hashing algorithm for fast, minimal-disruption lookups.
   - Provides better distribution and minimal remapping when servers change, using a permutation-based lookup table.

## Features
- **Modern interactive UI** (Thymeleaf + Bootstrap)
- Add/remove servers dynamically
- Simulate requests with custom keys, weights, or connections
- Select and test different load balancing algorithms
- **Visualize**:
  - Server pool as color-coded pills (with tooltips)
  - Hash ring for Consistent Hashing/Maglev (SVG, animated arrow, hash/key position, tooltips)
  - Step-by-step explanation of request mapping
- Minimal disruption demo for Consistent Hashing/Maglev

## Tech Stack
- Java 21
- Spring Boot 3.x
- Thymeleaf (for UI)
- Bootstrap 5 (for modern look)

## Getting Started
1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd load-balancing-algorithms
   ```
2. **Build and run the project**
   ```bash
   mvn spring-boot:run
   ```
   (Make sure you are in the project root, where `pom.xml` is located.)
3. **Access the UI**
   - Open [http://localhost:8080](http://localhost:8080) in your browser.

## Project Structure
- `src/` - Java source code and Thymeleaf templates
- `pom.xml` - Maven build file
- `README.md` - Project documentation 