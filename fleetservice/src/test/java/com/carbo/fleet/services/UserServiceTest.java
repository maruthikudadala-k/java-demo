
package com.carbo.fleet.services;

import com.carbo.fleet.model.User;
import com.carbo.fleet.repository.UserMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMongoDbRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> users = userService.getAll();

        // Assert
        assertEquals(Collections.emptyList(), users);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        List<User> users = userService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(Collections.emptyList(), users);
        verify(userRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserById() {
        // Arrange
        String userId = "user123";
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.getUser(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldReturnUserByUserName() {
        // Arrange
        String userName = "userName123";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.getUserByUserName(userName);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    public void shouldSaveUser() {
        // Arrange
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldUpdateUser() {
        // Arrange
        User user = new User();

        // Act
        userService.updateUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUser() {
        // Arrange
        String userId = "user123";

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}
