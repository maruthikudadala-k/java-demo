
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
        user.setPassword("password");
        user.setTitle("Mr.");
        user.setOrganizationId("org1");
        user.setAuthorities(Collections.emptyList());

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
        String organizationId = "org1";
        User user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setPassword("password");
        user.setTitle("Mr.");
        user.setOrganizationId(organizationId);
        user.setAuthorities(Collections.emptyList());

        Mockito.when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(user));

        // Act
        List<User> users = userService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(1, users.size());
        assertEquals(organizationId, users.get(0).getOrganizationId());
    }

    @Test
    public void shouldReturnUserById() {
        // Arrange
        String userId = "1";
        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.getUser(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
    }

    @Test
    public void shouldReturnUserByUserName() {
        // Arrange
        String userName = "johndoe";
        User user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName(userName);

        Mockito.when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userService.getUserByUserName(userName);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
    }

    @Test
    public void shouldSaveUser() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setFirstName("John");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals("John", savedUser.getFirstName());
    }

    @Test
    public void shouldUpdateUser() {
        // Arrange
        User user = new User();
        user.setId("1");
        user.setFirstName("John");

        // Act
        userService.updateUser(user);

        // Assert
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUserById() {
        // Arrange
        String userId = "1";

        // Act
        userService.deleteUser(userId);

        // Assert
        Mockito.verify(userRepository).deleteById(userId);
    }
}
