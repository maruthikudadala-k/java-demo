
package com.carbo.fleet.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@ExtendWith(MockitoJUnitRunner.class)
class HelloControllerTest {

    @InjectMocks
    private HelloController helloController;

    @Test
    void shouldReturnGreetingMessageWhenIndexIsCalled() {
        String result = helloController.index();
        assertThat(result).isEqualTo("Greetings from Spring Boot!");
    }
}
