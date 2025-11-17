
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
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setOrganizationId("org1");
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> users = userService.getAll();

        // Assert
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setOrganizationId("org1");
        Mockito.when(userRepository.findByOrganizationId("org1")).thenReturn(Collections.singletonList(user));

        // Act
        List<User> users = userService.getByOrganizationId("org1");

        // Assert
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getFirstName());
    }

    @Test
    public void shouldReturnUserById() {
        // Arrange
        User user = new User();
        user.setId("1");
        Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser("1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    public void shouldReturnUserByUserName() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setUserName("johndoe");
        Mockito.when(userRepository.findByUserName("johndoe")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUserName("johndoe");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getUserName());
    }

    @Test
    public void shouldSaveUser() {
        // Arrange
        User user = new User();
        user.setId("1");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.saveUser(user);

        // Assert
        assertEquals("1", result.getId());
    }

    @Test
    public void shouldUpdateUser() {
        // Arrange
        User user = new User();
        user.setId("1");
        Mockito.doNothing().when(userRepository).save(user);

        // Act
        userService.updateUser(user);

        // Assert
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUser() {
        // Arrange
        String userId = "1";
        Mockito.doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUser(userId);

        // Assert
        Mockito.verify(userRepository).deleteById(userId);
    }
}
