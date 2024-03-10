package net.binarypaper.springbootliquibase.greeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
public class GreetingControllerTest {

    private final RestClient restClient;

    public GreetingControllerTest(@LocalServerPort Integer port) {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @Test
    void testGenerateGreeting() {
        ResponseEntity<Greeting> response = restClient.get()
                .uri("/greetings/Willy")
                .retrieve()
                .toEntity(Greeting.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var greeting = response.getBody();
        assertNotNull(greeting);
        assertEquals("Hello Willy", greeting.getMessage());
    }

    @Test
    void testGetAllGreetings() {
        ResponseEntity<List<Greeting>> response = restClient.get()
                .uri("/greetings")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var greetings = response.getBody();
        assertNotNull(greetings);
        var greetingcountBefore = greetings.size();
        System.out.println("greetingcountBefore: " + greetingcountBefore);
        restClient.get().uri("/greetings/Willy").retrieve().toBodilessEntity();
        restClient.get().uri("/greetings/Adri").retrieve().toBodilessEntity();
        response = restClient.get()
                .uri("/greetings")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        greetings = response.getBody();
        assertNotNull(greetings);
        var greetingcountAfter = greetings.size();
        System.out.println("greetingcountAfter: " + greetingcountAfter);
        assertEquals(2, greetingcountAfter - greetingcountBefore);
    }
}