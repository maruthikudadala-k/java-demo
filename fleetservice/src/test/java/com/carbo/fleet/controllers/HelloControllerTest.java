
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class HelloControllerTest {

    @InjectMocks
    private HelloController helloController;

    @Test
    public void shouldReturnGreetingsMessageWhenIndexIsCalled() {
        // Act
        String result = helloController.index();
        
        // Assert
        assertThat(result).isEqualTo("Greetings from Spring Boot!");
    }
}
