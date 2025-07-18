package dev.habsgleich.orbit.repository;

import dev.habsgleich.orbit.Orbit;
import dev.habsgleich.orbit.customer.Customer;
import dev.habsgleich.orbit.query.QueryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.reflections.util.ClasspathHelper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class RepositoryTest {

    private static final Repository<Customer> REPOSITORY = Repository.of(Customer.class);

    @BeforeAll
    static void initialize() {
        System.out.println("Initialize Orbit Test...");
        Orbit.initialize(
            RepositoryTest.class.getResourceAsStream("/orbit.properties"),
            ClasspathHelper.forJavaClassPath()
        );
    }

    @Test
    @Order(1)
    void merge() {
        System.out.println("Merging...");
        Customer customer = REPOSITORY.merge(
            Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("email@email.com")
                .password("encryptedShit")
                .phone("1234567890")
                .build()
        );
        System.out.println(customer);

        assertNotNull(customer, "Customer is null");
        assertNotNull(customer.getId(), "Customer id was not auto-generated");
    }

    @Test
    @Order(2)
    void queryOne() {
        final Optional<Customer> customer = REPOSITORY.query()
            .equal("firstName", "John")
            .findOne();

        System.out.println(customer);

        assertNotNull(customer);
        assertTrue(customer.isPresent());
        assertNotNull(customer.get().getId());
        assertEquals("John", customer.get().getFirstName());
    }

    @Test
    @Order(3)
    void queryFilterFail() {
        final Optional<Customer> customer = REPOSITORY.query()
            .equal("firstName", "ShouldNotExist")
            .findOne();

        System.out.println(customer);

        assertNotNull(customer);
        assertFalse(customer.isPresent());
        assertThrows(NoSuchElementException.class, customer::get, "Result shouldn't exist, but an entry was found");
    }

    @Test
    @Order(4)
    void queryAll() {
        final List<Customer> customers = REPOSITORY.query().findAll();

        customers.forEach(System.out::println);

        assertNotNull(customers);
        assertEquals(1, customers.size(), "There should be only one customer");
    }

    @Test
    @Order(5)
    void delete() {
        QueryBuilder<Customer> query = REPOSITORY.query();
        Optional<Customer> customer = query.findOne();
        assertTrue(customer.isPresent(), "No customer found");

        REPOSITORY.delete(customer.get());

        customer = query.findOne();
        assertFalse(customer.isPresent(), "Customer was not deleted, no entry found");
    }
}