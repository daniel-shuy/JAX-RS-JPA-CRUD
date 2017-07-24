# JAX-RS-JPA-CRUD
Provides extendable classes to create JAX-RS CRUD Web Services tied to JPA entity classes.

The generated JAX-RS Web Services will contain:
- A `POST` method to add a row to the database table
- A `GET` method to query all rows in the database table
- A `GET` method to query a row by id
- A `PUT` method to update a row in the database table
- A `DELETE` method to delete a row in the database table

## Installation
This project is currently undergoing application to deploy to Maven Central. In the meantime, please clone, build and publish the project locally to use it.

## Usage
All CRUD database tables must have a sequential number Surrogate Primary Key.

For each CRUD database table:
- Create a JPA Entity Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.EntityCRUD`
- Create a Repository Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.RepositoryCRUD`
- Create a JAX-RS Resource Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.ResourceCRUD`.

### Example:
This example uses Dependency Injection (DI) to:
- Inject the `EntityManagerFactory` into the Repository Class
- Inject the Repository Class into the JAX-RS Resource Class.

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
public class UserRepository extends Repository<User> {
    @Inject
    public UserRepository(EntityManagerFactory entityManagerFactory) {
        super(User.class);
    }
}
```

#### Resource Class:
```java
@Path("/user")
public class UserResource extends ResourceCRUD<User, UserRepository> {
    @Inject
    public UserResource(UserRepository userRepository) {
        super(userRepository);
    }
}
```
