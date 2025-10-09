
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloControllerTest {

    private HelloController helloController = new HelloController();

    @Test
    void shouldReturnGreetingMessageWhenIndexIsCalled() {
        // Arrange
        String expected = "Greetings from Spring Boot!";

        // Act
        String actual = helloController.index();

        // Assert
        assertEquals(expected, actual);
    }
}
