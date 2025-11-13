
package com.carbo.fleet.services;

import com.carbo.fleet.model.User;
import com.carbo.fleet.repository.UserMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMongoDbRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnAllUsers() {
        // Arrange
        User user = new User();
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        User user = new User();
        Mockito.when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void shouldReturnUserById() {
        // Arrange
        String userId = "user123";
        User user = new User();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void shouldReturnUserByUserName() {
        // Arrange
        String userName = "testUser";
        User user = new User();
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUserName(userName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void shouldSaveUser() {
        // Arrange
        User user = new User();
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.saveUser(user);

        // Assert
        assertEquals(user, result);
    }

    @Test
    public void shouldUpdateUser() {
        // Arrange
        User user = new User();
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.updateUser(user);

        // Assert
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUser() {
        // Arrange
        String userId = "user123";

        // Act
        userService.deleteUser(userId);

        // Assert
        Mockito.verify(userRepository).deleteById(userId);
    }
}
