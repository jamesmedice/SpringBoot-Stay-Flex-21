#   User Service  

🧑‍💼 1. User Service

## The microservice stands for creating the project with an exposed api and a rest call to internal cluster service


📦 Models

#### java 
```
public enum Role { GUEST, HOST, ADMIN }

public record User(UUID id, String email, String passwordHash, Role role, LocalDate createdAt) {}

public record AuthRequest(String email, String password) {}
public record AuthResponse(String token, Role role) {}
```


✅ Activities
```
Register, login (JWT)

Role-based access (Host vs Guest)

Password encryption (BCrypt)

Use Java 21 record, switch, and SequencedCollection if needed
```
 
 ✨ MAVEN ✨
 
 
> Run mvn clean package -DskipTests
