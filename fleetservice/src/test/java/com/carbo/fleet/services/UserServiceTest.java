
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
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setOrganizationId("org123");
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void shouldReturnUsersByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        // Arrange
        String organizationId = "org123";
        User user = new User();
        user.setOrganizationId(organizationId);
        Mockito.when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(organizationId, result.get(0).getOrganizationId());
    }

    @Test
    public void shouldReturnUserWhenGetUserIsCalled() {
        // Arrange
        String userId = "userId123";
        User user = new User();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnUserWhenGetUserByUserNameIsCalled() {
        // Arrange
        String userName = "johndoe";
        User user = new User();
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUserName(userName);

        // Assert
        assertTrue(result.isPresent());
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
    }

    @Test
    public void shouldUpdateUserWhenUpdateUserIsCalled() {
        // Arrange
        User user = new User();
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.updateUser(user);

        // Assert
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUserWhenDeleteUserIsCalled() {
        // Arrange
        String userId = "userId123";

        // Act
        userService.deleteUser(userId);

        // Assert
        Mockito.verify(userRepository).deleteById(userId);
    }
}
