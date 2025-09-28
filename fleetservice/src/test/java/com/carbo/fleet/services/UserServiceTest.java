
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
        
        assertEquals(Collections.emptyList(), userService.getAll());
        verify(userRepository).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        String organizationId = "org123";
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), userService.getByOrganizationId(organizationId));
        verify(userRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserById() {
        String userId = "user123";
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(userId);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository).findById(userId);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserNotFoundById() {
        String userId = "user123";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(userId);
        assertFalse(result.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    public void shouldReturnUserByUserName() {
        String userName = "johndoe";
        User user = new User();
        user.setUserName(userName);
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName(userName);
        assertTrue(result.isPresent());
        assertEquals(userName, result.get().getUserName());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserNotFoundByUserName() {
        String userName = "johndoe";
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
        assertEquals(user, result);
        verify(userRepository).save(user);
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();
        userService.updateUser(user);
        verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUser() {
        String userId = "user123";
        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }
}
