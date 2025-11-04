
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HelloControllerTest {

    private final HelloController helloController = new HelloController();

    @Test
    public void shouldReturnGreetingsMessageWhenIndexIsCalled() {
        String result = helloController.index();
        assertEquals("Greetings from Spring Boot!", result);
    }
}
