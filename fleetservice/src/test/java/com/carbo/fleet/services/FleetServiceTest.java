
package com.carbo.fleet.services;

import com.carbo.fleet.events.model.FleetDetails;
import com.carbo.fleet.model.Error;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.model.PumpTypeEnum;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import com.carbo.fleet.repository.JobMongoDbRepository;
import com.carbo.fleet.repository.OnSiteEquipmentMongoDbRepository;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class FleetServiceTest {

    @Mock
    private FleetMongoDbRepository fleetRepository;

    @Mock
    private OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    @Mock
    private JobMongoDbRepository jobMongoDbRepository;

    @InjectMocks
    private FleetService fleetService;

    @Test
    public void shouldReturnFleetDataWhenValidRequest() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(String.class), eq("In Progress")))
                .thenReturn(Collections.singletonList(new Job()));

        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class)))
                .thenReturn(Collections.singletonList(new Fleet()));

        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any(Set.class)))
                .thenReturn(Collections.singletonList(new OnSiteEquipment()));

        // Act
        ResponseEntity responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccurs() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(String.class), eq("In Progress")))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(500, responseEntity.getStatusCodeValue());
        Error error = (Error) responseEntity.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
