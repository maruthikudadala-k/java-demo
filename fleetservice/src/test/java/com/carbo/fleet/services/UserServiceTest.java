
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMongoDbRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        String organizationId = "org-123";
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        List<User> result = userService.getByOrganizationId(organizationId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserById() {
        String userId = "user-123";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository).findById(userId);
    }

    @Test
    public void shouldReturnEmptyWhenUserIdNotFound() {
        String userId = "user-123";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(userId);

        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    public void shouldReturnUserByUserName() {
        String userName = "username";
        User user = new User();
        user.setUserName(userName);
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName(userName);

        assertTrue(result.isPresent());
        assertEquals(userName, result.get().getUserName());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    public void shouldReturnEmptyWhenUserNameNotFound() {
        String userName = "username";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName(userName);

        assertFalse(result.isPresent());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    public void shouldSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        userService.updateUser(user);

        verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUserById() {
        String userId = "user-123";

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}
