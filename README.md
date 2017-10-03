# JAX-RS-JPA-CRUD
Provides extendable classes to create JAX-RS CRUD Web Services from JPA entity classes.

The generated JAX-RS Web Services will contain:
- A `POST` method to add a row to the database table
- A `GET` method to query all rows in the database table
- A `GET` method to query a row by id
- A `PUT` method to update a row in the database table
- A `DELETE` method to delete a row in the database table

## Requirements
| Version | Java EE dependency |
| ------- | ------------------ |
| 1.X.X | Java EE 6 |
| 2.X.X | Java EE 7 |

## Usage
Add the following to your Maven dependency list:
```
<dependency>
    <groupId>com.github.daniel-shuy</groupId>
    <artifactId>jax-rs-jpa-crud</artifactId>
    <version>2.0.0</version>
</dependency>
```

Version 2.0.0+ uses CDI (Contexts and Dependency Injection).

The JPA [EntityManager](docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html) must be injected via CDI.

The injected EntityManager must be [@RequestScoped](http://docs.oracle.com/javaee/7/api/javax/enterprise/context/RequestScoped.html) to ensure each request will create a new EntityManager.

Example code to inject EntityManager:
```java
@ApplicationScoped
public class EntityManagerProducer {
    // ...
    
    @Produces
    @RequestScoped
    EntityManager createEntityManager() {
        // ...
    }
    
    void closeEntityManager(@Disposes EntityManager entityManager) {
        entityManager.close();
    }
}
```

__All CRUD database tables must have a sequential number Surrogate Primary Key.__

For each CRUD database table:
- Create a JPA Entity Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.EntityCRUD` (without a [@Column](https://docs.oracle.com/javaee/7/api/javax/persistence/Column.html) mapping for the Primary Key).
- Create an [@ApplicationScoped](docs.oracle.com/javaee/7/api/javax/enterprise/context/ApplicationScoped.html) Repository Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.RepositoryCRUD`
- Create a JAX-RS Resource Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.ResourceCRUD`.

### Example:
#### Entity Class:
```java
@Entity
@Table(name = "user")
public class User extends EntityCRUD {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;

    public User() {
    }

    public User(Long id) {
        super(id);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

#### Repository Class:
```java
@ApplicationScoped
public class UserRepository extends RepositoryCRUD<User> {
}
```

#### Resource Class:
```java
@Path("/user")
public class UserResource extends ResourceCRUD<User> {
}
```
