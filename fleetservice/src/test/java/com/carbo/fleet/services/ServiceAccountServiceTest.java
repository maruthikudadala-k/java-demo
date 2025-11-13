
package com.carbo.fleet.services;

import com.carbo.fleet.model.ServiceAccount;
import com.carbo.fleet.repository.ServiceAccountMongoDbRepository;
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
public class ServiceAccountServiceTest {

    @Mock
    private ServiceAccountMongoDbRepository serviceAccountMongoDbRepository;

    @InjectMocks
    private ServiceAccountService serviceAccountService;

    @Test
    public void shouldReturnAllServiceAccountsWhenGetAllIsCalled() {
        // Arrange
        Mockito.when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = serviceAccountService.getAll();

        // Assert
        assertEquals(Collections.emptyList(), result);
        Mockito.verify(serviceAccountMongoDbRepository).findAll();
    }

    @Test
    public void shouldReturnServiceAccountsWhenGetByOrganizationIdIsCalled() {
        // Arrange
        String organizationId = "org123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId))
                .thenReturn(Collections.singletonList(serviceAccount));

        // Act
        var result = serviceAccountService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(Collections.singletonList(serviceAccount), result);
        Mockito.verify(serviceAccountMongoDbRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnOptionalServiceAccountWhenGetIsCalled() {
        // Arrange
        String serviceAccountId = "sa123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findById(serviceAccountId))
                .thenReturn(Optional.of(serviceAccount));

        // Act
        var result = serviceAccountService.get(serviceAccountId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(serviceAccount, result.get());
        Mockito.verify(serviceAccountMongoDbRepository).findById(serviceAccountId);
    }

    @Test
    public void shouldSaveServiceAccountWhenSaveIsCalled() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // Act
        var result = serviceAccountService.save(serviceAccount);

        // Assert
        assertEquals(serviceAccount, result);
        Mockito.verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    public void shouldUpdateServiceAccountWhenUpdateIsCalled() {
        // Arrange
        ServiceAccount serviceAccount = new ServiceAccount();

        // Act
        serviceAccountService.update(serviceAccount);

        // Assert
        Mockito.verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    public void shouldDeleteServiceAccountWhenDeleteIsCalled() {
        // Arrange
        String serviceAccountId = "sa123";

        // Act
        serviceAccountService.delete(serviceAccountId);

        // Assert
        Mockito.verify(serviceAccountMongoDbRepository).deleteById(serviceAccountId);
    }
}
