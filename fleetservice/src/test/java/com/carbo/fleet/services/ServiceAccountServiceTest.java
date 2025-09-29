
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ServiceAccountServiceTest {

    @Mock
    private ServiceAccountMongoDbRepository serviceAccountMongoDbRepository;

    @InjectMocks
    private ServiceAccountService serviceAccountService;

    @Test
    void shouldReturnAllServiceAccounts() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.singletonList(serviceAccount));

        // Act
        var result = serviceAccountService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(serviceAccount, result.get(0));
    }

    @Test
    void shouldReturnServiceAccountsByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId))
                .thenReturn(Collections.singletonList(serviceAccount));

        // Act
        var result = serviceAccountService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(serviceAccount, result.get(0));
    }

    @Test
    void shouldReturnServiceAccountById() {
        // Arrange
        String serviceAccountId = "acc123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findById(serviceAccountId))
                .thenReturn(Optional.of(serviceAccount));

        // Act
        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(serviceAccount, result.get());
    }

    @Test
    void shouldSaveServiceAccount() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // Act
        ServiceAccount result = serviceAccountService.save(serviceAccount);

        // Assert
        assertEquals(serviceAccount, result);
    }

    @Test
    void shouldUpdateServiceAccount() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // Act
        serviceAccountService.update(serviceAccount);

        // Assert
        Mockito.verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    void shouldDeleteServiceAccountById() {
        // Arrange
        String serviceAccountId = "acc123";

        // Act
        serviceAccountService.delete(serviceAccountId);

        // Assert
        Mockito.verify(serviceAccountMongoDbRepository).deleteById(serviceAccountId);
    }
}
