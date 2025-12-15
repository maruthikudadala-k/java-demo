
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.junit.jupiter.MockitoSettings;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
public class HelloControllerTest {

    private HelloController helloController = new HelloController();

    @Test
    public void shouldReturnGreetingsWhenIndexIsCalled() {
        // When
        String response = helloController.index();

        // Then
        assertEquals("Greetings from Spring Boot!", response);
    }
}
