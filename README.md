# JAX-RS-JPA-CRUD
Provides extendable classes/interfaces to create JAX-RS CRUD Web Services from JPA entity classes.

The generated JAX-RS Web Services will contain:
- A `POST` method to add a row to the database table
- A `GET` method to query all rows in the database table
- A `GET` method to query a row by id
- A `PUT` method to update a row in the database table
- A `DELETE` method to delete a row in the database table

## Requirements
| Dependency | Version |
| ------- | ------------------ |
| JPA | 2.X.X |
| JAX-RS | 2.X |

## Usage
Add the following to your Maven dependency list:
```
<dependency>
    <groupId>com.github.daniel-shuy</groupId>
    <artifactId>jax-rs-jpa-crud</artifactId>
    <version>3.0.0</version>
</dependency>
```

Version 3.0.0+ no longer depends on JavaEE or CDI (Contexts and Dependency Injection). You can now use this outside of a JavaEE container, as long as a JPA and JAX-RS implementation is provided (eg. Apache Tomcat + Hibernate + Jersey).

__All CRUD database tables must have a sequential number Surrogate Primary Key.__

For each CRUD database table:
- Create a JPA Entity Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.EntityCRUD`, without a field for the Primary Key (already mapped in `EntityCRUD`).
- Create a Repository Class that implements `com.github.daniel.shuy.ws.rs.jpa.crud.RepositoryCRUD`
- Create a JAX-RS Resource Class that implements `com.github.daniel.shuy.ws.rs.jpa.crud.ResourceCRUD`.

Repository Classes can implement additional methods to 

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

#### Repository Class
Override `getEntityClass` to provide the JPA Entity Class.

Override `getEntityManager` to provide the JPA [EntityManager](https://static.javadoc.io/org.eclipse.persistence/javax.persistence/2.2.0/javax/persistence/EntityManager.html) instance to use. How to provide the EntityManager instance is entirely up to you.

##### @PersistenceContext Example
- Requires JavaEE
```java
@Stateless
@LocalBean
public class UserRepository implements RepositoryCRUD<User> {
    @PersistenceContext(name = "persistence-unit")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
```

##### CDI Example
- Requires CDI
```java
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface MySQLDatabase {}
```
- Qualifier annotation is optional if your application only has 1 persistence unit
```java
@ApplicationScoped
public class EntityManagerProducer {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence-unit");

    @Produces
    @RequestScoped
    @MySQLDatabase
    EntityManager createEntityManager() {
        return factory.createEntityManager();
    }

    void closeEntityManager(@Disposes @MySQLDatabase EntityManager entityManager) {
        entityManager.close();
    }
}

@ApplicationScoped
public class UserRepository implements RepositoryCRUD<User> {
    @Inject
    @MySQLDatabase
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
```

##### Barebones Example (no JavaEE/CDI)
- See [Barebones Example](#barebones-example) below

#### Resource Class

##### EJB Example
- Requires JavaEE
```java
@Path("/user")
@Stateless
public class UserResource implements ResourceCRUD<User> {
    @EJB
    private UserRepository repository;

    @Override
    public RepositoryCRUD<User> getRepository() {
        return repository;
    }
}
```

##### CDI Example:
- Requires CDI
```java
@Path("/user")
public class UserResource implements ResourceCRUD<User> {
    @Inject
    private UserRepository repository;

    @Override
    public RepositoryCRUD<User> getRepository() {
        return repository;
    }
}
```

##### Barebones Example (no JavaEE/CDI)
- See [Barebones Example](#barebones-example) below

### Barebones Example
- No additional dependencies.
- A lot more complicated because you have to manage the lifecycle of the EntityManager instances yourself.

#### Entity Class
- Same as [Entity Class](#entity-class) above

#### Repository Class:
- The EntityManager instance is passed in from the JAX-RS Resource (to get a new EntityManager instance for each request).
```java
public class UserRepository implements RepositoryCRUD<User> {
    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
```

#### Resource Class:
- By default, each JAX-RS Resource Class is instantiated once per-request. This example takes advantage of that behavior by creating the EntityManager instance in the constructor.
- `ResourceCRUD` exposes a `close` method that is executed after each request. Override it to close the EntityManager after each request.
```java
@Path("/user")
public class UserResource implements ResourceCRUD<User> {
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence-unit");

    private final EntityManager entityManager;
    private final UserRepository repository;

    public UserResource() {
        entityManager = factory.createEntityManager();
        repository = new UserRepository(entityManager);
    }

    @Override
    public void close() {
        entityManager.close();
    }
}
```
