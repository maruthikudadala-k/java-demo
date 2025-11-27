
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
    public void shouldReturnAllUsersWhenGetAllIsCalled() {
        // Arrange
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> users = userService.getAll();

        // Assert
        assertEquals(Collections.emptyList(), users);
        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        // Arrange
        String organizationId = "org123";
        Mockito.when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        List<User> users = userService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(Collections.emptyList(), users);
        Mockito.verify(userRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserWhenGetUserIsCalled() {
        // Arrange
        String userId = "user123";
        User user = new User();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    public void shouldReturnUserWhenGetUserByUserNameIsCalled() {
        // Arrange
        String userName = "testUser";
        User user = new User();
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUserName(userName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        Mockito.verify(userRepository).findByUserName(userName);
    }

    @Test
    public void shouldSaveUserWhenSaveUserIsCalled() {
        // Arrange
        User user = new User();
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.saveUser(user);

        // Assert
        assertEquals(user, result);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void shouldUpdateUserWhenUpdateUserIsCalled() {
        // Arrange
        User user = new User();

        // Act
        userService.updateUser(user);

        // Assert
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUserWhenDeleteUserIsCalled() {
        // Arrange
        String userId = "user123";

        // Act
        userService.deleteUser(userId);

        // Assert
        Mockito.verify(userRepository).deleteById(userId);
    }
}
