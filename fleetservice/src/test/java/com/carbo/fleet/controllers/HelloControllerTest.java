
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HelloControllerTest {

    private HelloController helloController = new HelloController();

    @Test
    public void shouldReturnGreetingWhenIndexIsCalled() {
        String result = helloController.index();
        assertEquals("Greetings from Spring Boot!", result);
    }
}
