#   User Service  

🧑‍💼 1. User Service

## The microservice stands for creating the project with an exposed api and a rest call to internal cluster service


📦 Models

#### java 
```
public enum Role { GUEST, HOST, ADMIN }

public record User(UUID id, String email, String passwordHash, Role role, LocalDate createdAt) {}

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

> Run using Intellij : 
> Set on run field : spring-boot:run -P test -Dspring-boot.run.profiles=test
> Set on environment vairables SPRING_PROFILES_ACTIVE=test
> Result: spring-boot:run -P test -Dspring-boot.run.profiles=test -DskipTests=true 


🧩 K8S change cluster 
kubectl config get-contexts

kubectl config use-context docker-desktop

CURRENT   NAME                                                                                                                                                                                         
*         docker-desktop                                                                                                                                                     
          gke_ita-pocdemodatalabs-dev-001_europe-west3-a_ddl-webapp-cluster   

