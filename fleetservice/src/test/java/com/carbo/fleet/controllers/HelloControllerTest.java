
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class HelloControllerTest {

    @InjectMocks
    private HelloController helloController;

    @Test
    public void shouldReturnGreetingMessageWhenIndexIsCalled() {
        // Act
        String result = helloController.index();

        // Assert
        assertEquals("Greetings from Spring Boot!", result);
    }
}
