
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
