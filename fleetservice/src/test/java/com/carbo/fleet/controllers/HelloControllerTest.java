
package com.carbo.fleet.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class HelloControllerTest {

    @Test
    public void shouldReturnGreetingWhenIndexIsCalled() {
        // Arrange
        HelloController controller = new HelloController();

        // Act
        String result = controller.index();

        // Assert
        assertEquals("Greetings from Spring Boot!", result);
    }
}
