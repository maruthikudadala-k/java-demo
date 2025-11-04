
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ServiceAccountServiceTest {

    @Mock
    private ServiceAccountMongoDbRepository serviceAccountMongoDbRepository;

    @InjectMocks
    private ServiceAccountService serviceAccountService;

    @Test
    void shouldReturnAllServiceAccounts() {
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.singletonList(serviceAccount));

        assertEquals(1, serviceAccountService.getAll().size());
    }

    @Test
    void shouldReturnServiceAccountsByOrganizationId() {
        String organizationId = "org123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(serviceAccount));

        assertEquals(1, serviceAccountService.getByOrganizationId(organizationId).size());
    }

    @Test
    void shouldReturnServiceAccountById() {
        String serviceAccountId = "account123";
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.of(serviceAccount));

        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenServiceAccountNotFound() {
        String serviceAccountId = "account123";
        Mockito.when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.empty());

        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveServiceAccount() {
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        ServiceAccount result = serviceAccountService.save(serviceAccount);
        assertEquals(serviceAccount, result);
    }

    @Test
    void shouldUpdateServiceAccount() {
        ServiceAccount serviceAccount = new ServiceAccount();
        Mockito.when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        serviceAccountService.update(serviceAccount);
        Mockito.verify(serviceAccountMongoDbRepository).save(serviceAccount);
    }

    @Test
    void shouldDeleteServiceAccount() {
        String serviceAccountId = "account123";
        serviceAccountService.delete(serviceAccountId);
        Mockito.verify(serviceAccountMongoDbRepository).deleteById(serviceAccountId);
    }
}
