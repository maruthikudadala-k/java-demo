
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
    public void shouldReturnAllUsersWhenGetAllIsCalled() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), userService.getAll());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), userService.getByOrganizationId(organizationId));
        verify(userRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserWhenGetUserIsCalled() {
        String userId = "user123";
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(userId);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldReturnEmptyWhenUserNotFoundInGetUserIsCalled() {
        String userId = "user123";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(userId);
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldReturnUserWhenGetUserByUserNameIsCalled() {
        String userName = "username";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName(userName);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    public void shouldReturnEmptyWhenUserNotFoundInGetUserByUserNameIsCalled() {
        String userName = "username";
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUserName(userName);
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    public void shouldSaveUserWhenSaveUserIsCalled() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldUpdateUserWhenUpdateUserIsCalled() {
        User user = new User();
        
        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUserWhenDeleteUserIsCalled() {
        String userId = "user123";

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
