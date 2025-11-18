
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private HelloController helloController;

    @Test
    public void shouldReturnGreetingMessageWhenIndexIsCalled() {
        String result = helloController.index();
        assertThat(result).isEqualTo("Greetings from Spring Boot!");
    }
}
