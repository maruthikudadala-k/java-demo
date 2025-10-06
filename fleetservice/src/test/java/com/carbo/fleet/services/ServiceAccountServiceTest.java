
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
        when(serviceAccountMongoDbRepository.findAll()).thenReturn(Collections.emptyList());

        var result = serviceAccountService.getAll();

        assertEquals(Collections.emptyList(), result);
        verify(serviceAccountMongoDbRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnServiceAccountsByOrganizationId() {
        String organizationId = "org123";
        when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        var result = serviceAccountService.getByOrganizationId(organizationId);

        assertEquals(Collections.emptyList(), result);
        verify(serviceAccountMongoDbRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnServiceAccountById() {
        String serviceAccountId = "account123";
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.of(serviceAccount));

        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);

        assertTrue(result.isPresent());
        assertEquals(serviceAccount, result.get());
        verify(serviceAccountMongoDbRepository, times(1)).findById(serviceAccountId);
    }

    @Test
    public void shouldSaveServiceAccount() {
        ServiceAccount serviceAccount = new ServiceAccount();
        when(serviceAccountMongoDbRepository.save(serviceAccount)).thenReturn(serviceAccount);

        ServiceAccount result = serviceAccountService.save(serviceAccount);

        assertEquals(serviceAccount, result);
        verify(serviceAccountMongoDbRepository, times(1)).save(serviceAccount);
    }

    @Test
    public void shouldUpdateServiceAccount() {
        ServiceAccount serviceAccount = new ServiceAccount();

        serviceAccountService.update(serviceAccount);

        verify(serviceAccountMongoDbRepository, times(1)).save(serviceAccount);
    }

    @Test
    public void shouldDeleteServiceAccountById() {
        String serviceAccountId = "account123";

        serviceAccountService.delete(serviceAccountId);

        verify(serviceAccountMongoDbRepository, times(1)).deleteById(serviceAccountId);
    }
}
