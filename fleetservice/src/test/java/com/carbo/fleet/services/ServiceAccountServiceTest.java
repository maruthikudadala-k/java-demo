
package com.carbo.fleet.services;

import com.carbo.fleet.model.ServiceAccount;
import com.carbo.fleet.repository.ServiceAccountMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.singletonList(serviceAccount));

        // Act
        List<ServiceAccount> result = serviceAccountService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(serviceAccount, result.get(0));
        verify(serviceAccountMongoDbRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnServiceAccountsByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(serviceAccount));

        // Act
        List<ServiceAccount> result = serviceAccountService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(serviceAccount, result.get(0));
        verify(serviceAccountMongoDbRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnServiceAccountById() {
        // Arrange
        String serviceAccountId = "account123";
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.of(serviceAccount));

        // Act
        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(serviceAccount, result.get());
        verify(serviceAccountMongoDbRepository, times(1)).findById(serviceAccountId);
    }

    @Test
    public void shouldSaveServiceAccount() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // Act
        ServiceAccount result = serviceAccountService.save(serviceAccount);

        // Assert
        assertEquals(serviceAccount, result);
        verify(serviceAccountMongoDbRepository, times(1)).save(serviceAccount);
    }

    @Test
    public void shouldUpdateServiceAccount() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        doNothing().when(serviceAccountMongoDbRepository).save(serviceAccount);

        // Act
        serviceAccountService.update(serviceAccount);

        // Assert
        verify(serviceAccountMongoDbRepository, times(1)).save(serviceAccount);
    }

    @Test
    public void shouldDeleteServiceAccountById() {
        // Arrange
        String serviceAccountId = "account123";
        doNothing().when(serviceAccountMongoDbRepository).deleteById(serviceAccountId);

        // Act
        serviceAccountService.delete(serviceAccountId);

        // Assert
        verify(serviceAccountMongoDbRepository, times(1)).deleteById(serviceAccountId);
    }
}
