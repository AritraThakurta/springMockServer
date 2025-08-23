# 🛠️ Mock API Service

A **Spring Boot-based Mock API Service** that loads mock endpoints dynamically from a `mocks.json` configuration file.  
It allows developers to simulate API responses without needing a real backend.

---

## 📂 Project Structure

```
src/main/java/com/At/springMockServer/
│
├── GlobalExceptionHandler.java   # Handles exceptions globally with structured error responses
├── MockController.java           # REST controller exposing mock endpoints
├── MockNotFoundException.java    # Custom exception for missing mock endpoints
├── MockEndpoint.java             # Model class representing a mock endpoint
├── MockEndpointService.java      # Service layer for managing mock endpoints
│
mocks.json                        # JSON config for defining mock endpoints
```

---

## ⚙️ Features

- ✅ File-driven mock endpoints (`mocks.json`)  
- ✅ Match requests by **method + path**  
- ✅ Match requests by **exact body** or **partial body contains**  
- ✅ Configurable **status codes, response bodies, and delays**  
- ✅ Centralized exception handling (`GlobalExceptionHandler`)  

---

## 🚀 Getting Started

### 1. Prerequisites
- Java 17+  
- Maven or Gradle  
- Spring Boot  

### 2. Clone the repository
```bash
git clone https://github.com/AritraThakurta/springMockServer.git
cd springMockServer
```

### 3. Configure your mocks
Edit `mocks.json` to define endpoints. Example:

```json
[
  {
    "method": "GET",
    "path": "/api/v1/users",
    "responseStatus": 200,
    "responseBody": "[{\"id\":1,\"name\":\"John Doe\"},{\"id\":2,\"name\":\"Jane Doe\"}]",
    "responseDelayMs": 0
  },
  {
    "method": "POST",
    "path": "/api/v1/users",
    "expectedRequestBody": "{\"name\":\"Jane\"}",
    "responseStatus": 201,
    "responseBody": "{\"message\": \"User Jane created\"}",
    "responseDelayMs": 0
  }
]
```

### 4. Run the service
If using **Maven**:
```bash
mvn spring-boot:run
```

If using **Gradle**:
```bash
./gradlew bootRun
```

Service will start on:  
👉 `http://localhost:8080`

---

## 📡 Example API Usage

### 1. GET Users
```http
GET /api/v1/users
```
Response:
```json
[
  { "id": 1, "name": "John Doe" },
  { "id": 2, "name": "Jane Doe" }
]
```

---

### 2. POST User (Jane ✅)
```http
POST /api/v1/users
Content-Type: application/json

{ "name": "Jane" }
```
Response:
```json
{ "message": "User Jane created" }
```

---

### 3. POST User (Ghost ❌)
```http
POST /api/v1/users
Content-Type: application/json

{ "name": "Ghost" }
```
Response:
```json
{ "error": "Ghost not allowed" }
```

---

### 4. POST User with Role = admin
```http
POST /api/v1/users
Content-Type: application/json

{ "name": "Alex", "role": "admin" }
```
Response:
```json
{ "message": "Admin created" }
```

---

### 5. POST User with Role = user
```http
POST /api/v1/users
Content-Type: application/json

{ "name": "Tom", "role": "user" }
```
Response:
```json
{ "message": "Regular user created" }
```

---

## ❌ Error Handling

If no matching mock is found, the service responds with `404 Not Found`:

```json
{
  "error": "Mock not found",
  "message": "No mock configured for /api/v1/users with given request",
  "status": 404,
  "timestamp": "2025-08-23T18:45:00"
}
```

---

## 🛠️ Technologies Used
- Java 17+  
- Spring Boot (Web, Validation)  
- Jackson (JSON handling)  
- Maven/Gradle  

---

## 📌 Future Enhancements
- Add UI to manage `mocks.json` dynamically  
- Persist mocks in a database  
- Support for headers-based matching  
- Record-and-replay feature for real APIs  
