
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoJUnitRunner.class)
public class FleetServiceTest {

    @InjectMocks
    private FleetService fleetService;

    @Mock
    private FleetMongoDbRepository fleetRepository;

    @Mock
    private OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    @Mock
    private JobMongoDbRepository jobMongoDbRepository;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        // Set up mock behavior for the request
        when(request.getUserPrincipal()).thenReturn(new MockPrincipal());
    }

    @Test
    public void shouldReturnAllFleetsWhenGetAllCalled() {
        // Arrange
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getAll();

        // Assert
        assertEquals(fleets, result);
    }

    @Test
    public void shouldReturnFleetListWhenGetByOrganizationIdCalled() {
        // Arrange
        String organizationId = "org1";
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(fleets, result);
    }

    @Test
    public void shouldReturnOptionalFleetWhenGetFleetCalled() {
        // Arrange
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetService.getFleet(fleetId);

        // Assert
        assertEquals(Optional.of(fleet), result);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        when(fleetRepository.save(any(Fleet.class))).thenReturn(fleet);

        // Act
        Fleet result = fleetService.saveFleet(fleet);

        // Assert
        assertEquals(fleet, result);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        doNothing().when(fleetRepository).save(any(Fleet.class));

        // Act
        fleetService.updateFleet(fleet);

        // Assert
        verify(fleetRepository, times(1)).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        // Arrange
        String fleetId = "fleet1";
        doNothing().when(fleetRepository).deleteById(fleetId);

        // Act
        fleetService.deleteFleet(fleetId);

        // Assert
        verify(fleetRepository, times(1)).deleteById(fleetId);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataCalled() {
        // Arrange
        String organizationId = "org1";
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> expectedFleetData = new HashMap<>();
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);
        List<Job> jobList = Collections.singletonList(job);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(jobList);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.singletonList(new Fleet()));

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedFleetData, result.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInGetFleetData() {
        // Arrange
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(), any())).thenThrow(new RuntimeException());

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody() instanceof Error);
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
    
    private class MockPrincipal implements Principal {
        @Override
        public String getName() {
            return "testUser";
        }
    }
}
