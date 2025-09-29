
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
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnUsersByOrganizationId() {
        String organizationId = "orgId";
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());
        
        assertEquals(Collections.emptyList(), userService.getByOrganizationId(organizationId));
        verify(userRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnUserById() {
        String id = "userId";
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        
        assertEquals(Optional.of(user), userService.getUser(id));
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void shouldReturnUserByUserName() {
        String userName = "userName";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
        
        assertEquals(Optional.of(user), userService.getUserByUserName(userName));
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    public void shouldSaveUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);
        
        assertEquals(user, userService.saveUser(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldUpdateUser() {
        User user = new User();
        
        userService.updateUser(user);
        
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldDeleteUserById() {
        String userId = "userId";
        
        userService.deleteUser(userId);
        
        verify(userRepository, times(1)).deleteById(userId);
    }
}
