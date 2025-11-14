
package com.carbo.fleet.services;

import com.carbo.fleet.model.User;
import com.carbo.fleet.repository.UserMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMongoDbRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsersWhenGetAllIsCalled() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        assertEquals(2, userService.getAll().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUsersByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        User user = new User();
        when(userRepository.findByOrganizationId(organizationId)).thenReturn(Arrays.asList(user));

        assertEquals(1, userService.getByOrganizationId(organizationId).size());
        verify(userRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    void shouldReturnUserWhenGetUserIsCalled() {
        String id = "user123";
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUser(id);
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnUserWhenGetUserByUserNameIsCalled() {
        String userName = "testUser";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUserName(userName);
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    void shouldSaveUserWhenSaveUserIsCalled() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldUpdateUserWhenUpdateUserIsCalled() {
        User user = new User();
        userService.updateUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldDeleteUserWhenDeleteUserIsCalled() {
        String userId = "user123";
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
