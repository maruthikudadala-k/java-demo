
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HelloControllerTest {
    
    @InjectMocks
    private HelloController helloController;

    @Test
    public void shouldReturnGreetingsMessageWhenIndexIsCalled() {
        // Given
        String expectedMessage = "Greetings from Spring Boot!";
        
        // When
        String actualMessage = helloController.index();
        
        // Then
        assertEquals(expectedMessage, actualMessage);
    }
}
