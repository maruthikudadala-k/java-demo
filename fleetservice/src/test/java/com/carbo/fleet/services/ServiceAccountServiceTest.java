
package com.carbo.fleet.services;

import com.carbo.fleet.model.ServiceAccount;
import com.carbo.fleet.repository.ServiceAccountMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceAccountServiceTest {

    @Mock
    private ServiceAccountMongoDbRepository serviceAccountMongoDbRepository;

    @InjectMocks
    private ServiceAccountService serviceAccountService;

    @Test
    public void shouldReturnAllServiceAccounts() {
        // Arrange
        when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = serviceAccountService.getAll();

        // Assert
        assertEquals(Collections.emptyList(), result);
        verify(serviceAccountMongoDbRepository).findAll();
    }

    @Test
    public void shouldReturnServiceAccountsByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Act
        var result = serviceAccountService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(Collections.emptyList(), result);
        verify(serviceAccountMongoDbRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnServiceAccountById() {
        // Arrange
        String serviceAccountId = "account123";
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.of(serviceAccount));

        // Act
        var result = serviceAccountService.get(serviceAccountId);

        // Assert
        assertEquals(Optional.of(serviceAccount), result);
        verify(serviceAccountMongoDbRepository).findById(serviceAccountId);
    }

    @Test
    public void shouldSaveServiceAccount() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // Act
        var result = serviceAccountService.save(serviceAccount);

        // Assert
        assertEquals(serviceAccount, result);
        verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    public void shouldUpdateServiceAccount() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // Act
        serviceAccountService.update(serviceAccount);

        // Assert
        verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    public void shouldDeleteServiceAccountById() {
        // Arrange
        String serviceAccountId = "account123";

        // Act
        serviceAccountService.delete(serviceAccountId);

        // Assert
        verify(serviceAccountMongoDbRepository).deleteById(serviceAccountId);
    }
}
