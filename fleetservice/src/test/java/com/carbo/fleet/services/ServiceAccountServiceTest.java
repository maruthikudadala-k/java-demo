
package com.carbo.fleet.services;

import com.carbo.fleet.model.ServiceAccount;
import com.carbo.fleet.repository.ServiceAccountMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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
        ServiceAccount account1 = new ServiceAccount();
        ServiceAccount account2 = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        assertEquals(2, serviceAccountService.getAll().size());
        verify(serviceAccountMongoDbRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnServiceAccountsByOrganizationId() {
        String organizationId = "org123";
        ServiceAccount account = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findByOrganizationId(organizationId)).thenReturn(Collections.singletonList(account));

        assertEquals(1, serviceAccountService.getByOrganizationId(organizationId).size());
        verify(serviceAccountMongoDbRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnOptionalServiceAccountById() {
        String serviceAccountId = "account123";
        ServiceAccount account = new ServiceAccount();
        when(serviceAccountMongoDbRepository.findById(serviceAccountId)).thenReturn(Optional.of(account));

        Optional<ServiceAccount> result = serviceAccountService.get(serviceAccountId);
        assertEquals(true, result.isPresent());
        verify(serviceAccountMongoDbRepository, times(1)).findById(serviceAccountId);
    }

    @Test
    public void shouldSaveServiceAccount() {
        ServiceAccount account = new ServiceAccount();
        when(serviceAccountMongoDbRepository.save(account)).thenReturn(account);

        ServiceAccount savedAccount = serviceAccountService.save(account);
        assertEquals(account, savedAccount);
        verify(serviceAccountMongoDbRepository, times(1)).save(account);
    }

    @Test
    public void shouldUpdateServiceAccount() {
        ServiceAccount account = new ServiceAccount();
        serviceAccountService.update(account);
        verify(serviceAccountMongoDbRepository, times(1)).save(account);
    }

    @Test
    public void shouldDeleteServiceAccountById() {
        String serviceAccountId = "account123";
        serviceAccountService.delete(serviceAccountId);
        verify(serviceAccountMongoDbRepository, times(1)).deleteById(serviceAccountId);
    }
}
