# UK Power Engineering

The website is deployed and accessible at https://uk-power-engineering.onrender.com.

This is a web-application that shows the best interval for energy consumption in the UK. It uses this public API: https://carbon-intensity.github.io/api-definitions/?shell#get-generation-from-to

The backend is written in Java + Spring Boot and the frontend is written in React + Vite.

## Fast start (Docker)

### Instructions
1. Clone the repository (or download the project folder).
2. Open a terminal in the root of the project.
3. Run the command:
   ```bash
   docker-compose up --build
   ```
4. Wait for the build and container startup to complete.
5. Open the application in your browser: [http://localhost:8080](http://localhost:8080)

## Development mode (Local run)

### Requirements
- **Node.js** (version 20 or higher)
- **Java JDK** (version 17 or higher)

### 1. Run Frontend (UI)
```bash
cd UI
npm install
npm run dev
```
Application will be available at: `http://localhost:5173` (or another address specified in the console).

### 2. Run Backend (API)
```bash
cd API
./mvnw spring-boot:run
```
API will run on port `8081`.
