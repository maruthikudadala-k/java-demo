
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
        User user = new User();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> users = userService.getAll();

        // Assert
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        User user = new User();
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(user));

        // Act
        List<User> users = userService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserById() {
        // Arrange
        String id = "user123";
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(id);

        // Assert
        assertTrue(result.isPresent());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnUserByUserName() {
        // Arrange
        String userName = "username";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUserName(userName);

        // Assert
        assertTrue(result.isPresent());
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
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.updateUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUserById() {
        // Arrange
        String id = "user123";

        // Act
        userService.deleteUser(id);

        // Assert
        verify(userRepository, times(1)).deleteById(id);
    }
}
