
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
    public void shouldReturnAllServiceAccounts() {
        // Given
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.singletonList(serviceAccount));

        // When
        List<ServiceAccount> result = serviceAccountService.getAll();

        // Then
        assertEquals(1, result.size());
        assertEquals(serviceAccount, result.get(0));
    }

    @Test
    public void shouldReturnServiceAccountsByOrganizationId() {
        // Given
        String organizationId = "org123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(serviceAccount));

        // When
        List<ServiceAccount> result = serviceAccountService.getByOrganizationId(organizationId);

        // Then
        assertEquals(1, result.size());
        assertEquals(serviceAccount, result.get(0));
    }

    @Test
    public void shouldReturnServiceAccountById() {
        // Given
        String serviceAccountId = "acc123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.of(serviceAccount));

        // When
        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(serviceAccount, result.get());
    }

    @Test
    public void shouldSaveServiceAccount() {
        // Given
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // When
        ServiceAccount result = serviceAccountService.save(serviceAccount);

        // Then
        assertEquals(serviceAccount, result);
    }

    @Test
    public void shouldUpdateServiceAccount() {
        // Given
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        // When
        serviceAccountService.update(serviceAccount);

        // Then
        Mockito.verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    public void shouldDeleteServiceAccount() {
        // Given
        String serviceAccountId = "acc123";

        // When
        serviceAccountService.delete(serviceAccountId);

        // Then
        Mockito.verify(serviceAccountMongoDbRepository).deleteById(serviceAccountId);
    }
}
