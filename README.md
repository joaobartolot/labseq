# 🧮 Labseq Calculator

This is a small full-stack project that implements a Labseq sequence calculator. It includes a REST API built with Quarkus and an optional Angular for interaction — all containerized using Docker.

The project was built as part of a technical exercise, and aims to demonstrate clean implementation, caching, and containerization.

---

## 💡 What is Labseq?

The Labseq sequence is defined as:

```
l(0) = 0  
l(1) = 1  
l(2) = 0  
l(3) = 1  
l(n) = l(n - 4) + l(n - 3), for n > 3
```

---

## 🧠 Thought process

The first thing that came to mind was solving it recursively and caching results with memoization (later using Redis). That worked for small numbers, but it quickly ran into stack overflows for larger inputs.

So I rethought the approach: since each new value depends only on the last four, I could simply **track those four values in a sliding window**. Then, I’d:

1. Add the first and second values
2. Push the result into the queue
3. Remove the oldest value

This made the implementation fast, simple, and safe — no recursion needed.

---

## 📦 What's inside

### 🔧 Backend – Java (Quarkus)
- REST endpoint: `GET /labseq/{n}`
- Efficient iterative implementation (sliding window)
- Redis used to cache results
- Swagger UI available at:  
  [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)
- Includes recursive implementation for reference

### 💻 Frontend – Angular
- Input field and submit button
- Validates input (no negatives)
- Shows result

---

## 🚀 How to Run

Just make sure you have Docker installed.

Then run from the project root:

```bash
docker-compose up --build
```

This will bring up the backend, frontend, and Redis with a single command.

- 🧾 Swagger: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)
- 🧮 API: [http://localhost:8080/labseq/10](http://localhost:8080/labseq/10)
- 💻 Frontend: [http://localhost:4200](http://localhost:4200)

---

## 🧪 Example

Calling:

```
GET /labseq/10
```

Returns:

```
4
```

---

## ⚙️ Tech Stack

- Java 17 + Quarkus
- Angular
- Redis
- Docker & Docker Compose
- Swagger (OpenAPI)

---

## 📝 Notes

- The recursive method is included for learning purposes but not used in practice
- The main goal here was performance, clarity, and ease of setup
- Docker handles the full environment — no need to install anything else

---

Thanks for reviewing this solution! Let me know if you'd like improvements or additions.
