
# Spring Boot Book API

A simple Spring Boot application with MySQL that provides REST APIs to manage books.

---

## 🛠️ Tech Stack
- Java 17
- Spring Boot 3
- MySQL 8+
- Maven (build tool)

---

## 📚 API Endpoints

| Method | Endpoint           | Description           |
|--------|--------------------|-----------------------|
| GET    | `/api/books`       | Retrieve all books    |
| GET    | `/api/books/{id}`  | Retrieve book by ID   |
| POST   | `/api/books`       | Add a new book        |
| PUT    | `/api/books/{id}`  | Update existing book  |
| DELETE | `/api/books/{id}`  | Delete book by ID     |

---

## 🔧 Setup & Run Locally

### 1. Install and run MySQL

- Download & install MySQL: https://dev.mysql.com/downloads/
- Start the MySQL server (usually runs as a service or background process).
- Open MySQL command line or a client tool and create the database:

```sql
CREATE DATABASE bookdb;
```

* (Optional) Create a dedicated user with permissions or use root.

---

### 2. Configure database credentials

Edit `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookdb
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

Replace `YOUR_DB_USERNAME` and `YOUR_DB_PASSWORD` with your actual MySQL username and password.

---

### 3. Install Java & Maven

* Install Java 17 JDK: [https://adoptium.net/](https://adoptium.net/)
* Install Maven: [https://maven.apache.org/install.html](https://maven.apache.org/install.html)
* Make sure `java` and `mvn` commands work in your terminal (add to PATH if needed).

---

### 4. Run the Spring Boot app

Open a terminal in your project directory (where `pom.xml` is located) and run:

```bash
mvn spring-boot:run
```

You should see logs indicating the app has started and is listening on port 8080.

---

## 🌐 Optional Frontend

Open the included `index.html` file in a browser to add books via a simple form that interacts with the API.

---

## 📋 Test API with cURL

### Get all books

```bash
curl http://localhost:8080/api/books
```

---

### Get book by ID (example ID = 1)

```bash
curl http://localhost:8080/api/books/1
```

---

### Add a new book

**Windows CMD** (escape quotes properly):

```cmd
curl -X POST http://localhost:8080/api/books -H "Content-Type: application/json" -d "{\"title\":\"My First Book\",\"author\":\"Your Name\",\"year\":2025}"
```

**PowerShell** example:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/books -Method POST -ContentType "application/json" -Body '{"title":"My First Book","author":"Your Name","year":2025}'
```

---

### Update a book (example ID = 1)

```bash
curl -X PUT http://localhost:8080/api/books/1 -H "Content-Type: application/json" -d "{\"title\":\"Updated Title\",\"author\":\"New Author\",\"year\":2024}"
```

---

### Delete a book (example ID = 1)

```bash
curl -X DELETE http://localhost:8080/api/books/1
```

---

## 💡 Notes

* The app automatically creates and updates tables in the database (`spring.jpa.hibernate.ddl-auto=update`).
* Default server port: `8080` (changeable in `application.properties`).
* Use Postman or similar API tools for easier testing.
* Ensure MySQL server is running before starting the app.

---

Feel free to open issues or submit pull requests to improve the project!
