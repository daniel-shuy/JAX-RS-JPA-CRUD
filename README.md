# JAX-RS-JPA-CRUD
Provides extendable classes/interfaces to create JAX-RS CRUD Web Services from JPA entity classes.

The generated JAX-RS Web Services will contain:
- A `POST` method to insert a row
- A `GET` method to query all rows
- A `/{id}` `GET` method to query a row by id
- A `/{from}/{to}` `GET` method to query rows within the given range of ids
- A `/count` `GET` method to query the number of rows
- A `PUT` method to update a row
- A `/{id}` `DELETE` method to delete a row

## Requirements
| Dependency | Version |
| ------- | ------------------ |
| JPA | 2.X.X |
| JAX-RS | 2.X |
| JTA | 1.2 |

The easiest way to provide the implementation for all the dependencies is to use a Java EE container (eg. [WildFly](http://wildfly.org/)).

Version 3.0.0+ no longer depends on Java EE or CDI (Contexts and Dependency Injection). You can now use this outside of a Java EE container, as long as a JPA, JAX-RS and JTA implementation is provided (eg. [Hibernate](http://hibernate.org/orm/) + [Jersey](https://jersey.github.io/) + [Narayana](http://narayana.io/)).

## Usage
Add the following to your Maven dependency list:
```
<dependency>
    <groupId>com.github.daniel-shuy</groupId>
    <artifactId>jax-rs-jpa-crud</artifactId>
    <version>3.1.0</version>
</dependency>
```

__All CRUD database tables must have a sequential number Surrogate Primary Key.__

For each CRUD database table:
- Create a JPA Entity Class that extends `com.github.daniel.shuy.ws.rs.jpa.crud.EntityCRUD`, without a field for the Primary Key (already mapped in `EntityCRUD`).
- Create a Repository Class that implements `com.github.daniel.shuy.ws.rs.jpa.crud.RepositoryCRUD`
- Create a JAX-RS Resource Class that implements `com.github.daniel.shuy.ws.rs.jpa.crud.ResourceCRUD`.

Resource/Repository Classes can be further customized/extended by implementing additional methods.

### Example:
#### Entity Class:
```java
@Entity
@Table(name = "user")
@XmlRootElement
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
- Override `getEntityClass` to provide the JPA Entity Class.
- Override `getEntityManager` to provide the JPA [EntityManager](https://static.javadoc.io/org.eclipse.persistence/javax.persistence/2.2.0/javax/persistence/EntityManager.html) instance to use. How to provide the EntityManager instance depends whether you want to use Enterprise JavaBean (EJB) or Contexts and Dependency Injection (CDI).

##### EJB Example
- Requires Repository Class to be an Enterprise JavaBean (EJB)
```java
@Stateless
@LocalBean
public class UserRepository implements RepositoryCRUD<User> {
    @PersistenceContext(name = "persistence-unit")  // name is Persistence Unit Name configured in persistence.xml
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
- Requires Contexts and Dependency Injection (CDI)
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
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence-unit");    // input argument is Persistence Unit Name configured in persistence.xml

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

#### Resource Class

##### EJB Example
```java
@Path("/user")
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
