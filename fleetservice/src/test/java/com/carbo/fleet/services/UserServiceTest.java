
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
    public void shouldReturnAllUsersWhenGetAllIsCalled() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> users = userService.getAll();

        // Assert
        assertEquals(Collections.emptyList(), users);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationIdWhenGetByOrganizationIdIsCalled() {
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
    public void shouldReturnUserWhenGetUserIsCalled() {
        // Arrange
        String userId = "user123";
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> retrievedUser = userService.getUser(userId);

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertEquals(user, retrievedUser.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldReturnUserByUserNameWhenGetUserByUserNameIsCalled() {
        // Arrange
        String userName = "username";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> retrievedUser = userService.getUserByUserName(userName);

        // Assert
        assertTrue(retrievedUser.isPresent());
        assertEquals(user, retrievedUser.get());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    public void shouldSaveUserWhenSaveUserIsCalled() {
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
    public void shouldUpdateUserWhenUpdateUserIsCalled() {
        // Arrange
        User user = new User();

        // Act
        userService.updateUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUserWhenDeleteUserIsCalled() {
        // Arrange
        String userId = "user123";

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}
