
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoJUnitRunner.class)
class HelloControllerTest {

    private final HelloController helloController = new HelloController();

    @Test
    void shouldReturnGreetingMessageWhenIndexIsCalled() {
        String result = helloController.index();
        assertThat(result).isEqualTo("Greetings from Spring Boot!");
    }
}
