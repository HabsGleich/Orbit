# Orbit

Eine fluent-style Java ORM-Bibliothek für typsichere und elegante Datenbankoperationen, basierend auf [Hibernate](https://github.com/hibernate).

![Java Version](https://img.shields.io/badge/Java-8%2B-blue)
![License](https://img.shields.io/github/license/habsgleich/orbit)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)

## Übersicht

Orbit ist ein modernes Java ORM-Framework, das eine intuitive und typsichere API für Datenbankoperationen bietet. Es wurde entwickelt, um die Komplexität von SQL-Abfragen zu reduzieren und gleichzeitig volle Kontrolle über die generierten Abfragen zu behalten.

```java
// Beispiel einer Orbit-Abfrage
Optional<Customer> customer = Repository.of(Customer.class)
    .query()
    .equal("firstName", "John")
    .findOne();
```

## Features

- **Fluent API**: Intuitive Methodenverkettung für natürliche Lesbarkeit
- **Typsicherheit**: Kompilierzeitvalidierung von Abfragen und Entitäten
- **Repository-Pattern**: Klares und einfaches Design für Datenzugriffslogik
- **Benutzerdefinierte Abfragen**: Flexible `CustomQuery`-Schnittstelle für komplexe Szenarien
- **Transaktionskontrolle**: Einfache Verwaltung von Datenbanktransaktionen mit `OrbitTransaction`
- **Hibernate-Integration**: Nutzt die Leistungsfähigkeit von Hibernate mit einer vereinfachten API
- **Minimaler Boilerplate**: Generiere weniger Code für häufige Operationen
- **Einfache Konfiguration**: Schneller Einstieg mit unkomplizierter Einrichtung
- **Testfreundlich**: In-Memory-Datenbank-Integration für Tests
- **Leichtgewichtig**: Fokussiert auf das Wesentliche bei minimalen Abhängigkeiten

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("dev.habsgleich:orbit:<version>")
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>dev.habsgleich</groupId>
        <artifactId>orbit</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Schnellstart

### 1. Entitäten definieren

Erstelle deine Entitätsklassen mit den entsprechenden JPA-Annotationen:

```java
@Entity(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phone;
    private String password;
    
    // Getters und Setters oder verwende Lombok für weniger Boilerplate-Code
    
    // Optional: Builder-Pattern
    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }
}
```

Wichtige Annotationen für Entitäten:
- `@Entity`: Markiert die Klasse als JPA-Entität (mit optionalem Namen für die Tabelle)
- `@Id`: Definiert den Primärschlüssel
- `@GeneratedValue`: Konfiguriert die automatische ID-Generierung
- `@Column`: Passt die Spalteneigenschaften an (z.B. unique, nullable, length)
- `@Table`: Erlaubt weitere Tabellenkonfiguration (falls nötig)
- `@OneToMany`, `@ManyToOne`, etc.: Definiert Beziehungen zu anderen Entitäten

### 2. Orbit initialisieren

Orbit bietet mehrere Möglichkeiten zur Initialisierung:

#### Mit Properties-Datei als InputStream

```java
// Initialisiere Orbit mit Konfigurationsdatei und Entitätsklassen
Orbit.initialize(
    YourApp.class.getResourceAsStream("/orbit.properties"), 
    Customer.class
);
```

#### Mit Properties-Objekt

```java
// Eigenschaften direkt als Properties-Objekt
Properties props = new Properties();
props.setProperty("hibernate.hikari.dataSource.url", "jdbc:postgresql://localhost:5432/mydb");
props.setProperty("hibernate.hikari.dataSource.user", "user");
props.setProperty("hibernate.hikari.dataSource.password", "password");

Orbit.initialize(props, Customer.class);
```

#### Mit direkten Verbindungsparametern

```java
// Einfache Initialisierung mit Verbindungsparametern
Orbit.initialize(
    "jdbc:postgresql://localhost:5432/mydb",
    "user",
    "password",
    Customer.class
);
```

### 3. Repository erstellen und nutzen

```java
// Repository für spezifische Entität erstellen
Repository<Customer> repository = Repository.of(Customer.class);

// Neue Entität speichern
Customer customer = repository.merge(
    Customer.builder()
        .firstName("John")
        .lastName("Doe")
        .email("email@email.com")
        .password("encryptedPassword")
        .phone("1234567890")
        .build()
);

// Entität nach Kriterien abfragen
Optional<Customer> foundCustomer = repository.query()
    .equal("firstName", "John")
    .findOne();

// Alle Entitäten abrufen
List<Customer> allCustomers = repository.query().findAll();

// Entität löschen
repository.delete(customer);
```

## Abfragen

### Einfache Abfragen

```java
// Eine Entität finden
Optional<Customer> customer = repository.query()
    .equal("email", "test@example.com")
    .findOne();

// Mehrere Entitäten finden
List<Customer> customers = repository.query()
    .like("lastName", "Smith")
    .findAll();
```

### Komplexe Abfragen

```java
List<Customer> customers = repository.query()
    .equal("active", true)
    .or(q -> q
        .notEqual("status", "INACTIVE")
        .greaterThan("lastLogin", LocalDate.now().minusDays(30))
    )
    .orderBy("lastName", true)
    .limit(20)
    .findAll();
```

### Benutzerdefinierte Abfragen mit CustomQuery

Für komplexe Szenarien kannst du mit `CustomQuery` eigene Kriterien definieren:

```java
// CustomQuery funktionales Interface
Optional<Customer> vipCustomer = repository.query()
    .custom((criteriaBuilder, root) -> 
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get("status"), "VIP"),
            criteriaBuilder.greaterThan(root.get("totalPurchases"), 10000)
        )
    )
    .findOne();

// Kombiniere Standard-API mit benutzerdefinierten Abfragen
List<Customer> recentActiveVIPs = repository.query()
    .equal("active", true)
    .custom((cb, root) -> 
        cb.greaterThan(root.get("lastPurchaseDate"), LocalDate.now().minusDays(30))
    )
    .orderBy("totalPurchases", false) // absteigend
    .findAll();
```


## Benutzerdefinierte Transaktionen

Orbit ermöglicht dir, komplexe Transaktionen mit vollständiger Kontrolle über die Session und Transaktionsverwaltung auszuführen:

```java
// Benutzerdefinierte Transaktion mit dem OrbitTransaction-Interface
Customer result = repository.transactional((session, transaction) -> {
    // Führe mehrere Datenbankoperationen innerhalb einer Transaktion aus
    Customer customer = session.get(Customer.class, 1L);
    customer.setStatus("PREMIUM");
    
    Order newOrder = new Order();
    newOrder.setCustomer(customer);
    newOrder.setAmount(199.99);
    session.save(newOrder);
    
    // Die Transaktion wird automatisch committed und die Session geschlossen
    return customer;
});

// Fehlerbehandlung erfolgt automatisch mit Rollback
try {
    repository.transactional((session, transaction) -> {
        // Fehlerhafte Operation
        throw new RuntimeException("Etwas ist schiefgelaufen");
    });
} catch (RuntimeException e) {
    // Transaktion wurde automatisch zurückgerollt
    System.out.println("Transaktion wurde zurückgerollt: " + e.getMessage());
}
```

## Konfiguration

Erstelle eine `orbit.properties` Datei im Ressourcen-Verzeichnis:

```properties
# Datenbankverbindung mit Hibernate/HikariCP
hibernate.hikari.dataSource.url=jdbc:postgresql://localhost:5432/mydb
hibernate.hikari.dataSource.user=user
hibernate.hikari.dataSource.password=password

# Optionale Hibernate-Konfigurationen
hibernate.show_sql=true
hibernate.format_sql=true
hibernate.hbm2ddl.auto=update
```

### Zugriff auf die SessionFactory

Nachdem Orbit initialisiert wurde, kannst du bei Bedarf auf die Hibernate-SessionFactory zugreifen:

```java
// Nach der Initialisierung
SessionFactory sessionFactory = Orbit.instance().sessionFactory();
```

## Mitwirken

Beiträge sind willkommen! Bitte lies zuerst unsere [Beitragsrichtlinien](CONTRIBUTING.md).

## Lizenz

Dieses Projekt steht unter der MIT License - siehe die [LICENSE](LICENSE) Datei für Details.

## Dank

- Dank an alle Mitwirkenden und Benutzer, die Feedback geben
- Basiert auf [Hibernate](https://github.com/hibernate), einem der leistungsstärksten ORM-Frameworks für Java
