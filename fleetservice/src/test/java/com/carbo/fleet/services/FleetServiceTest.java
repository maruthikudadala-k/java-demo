
package com.carbo.fleet.services;

import com.carbo.fleet.events.model.FleetDetails;
import com.carbo.fleet.model.Error;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.model.PumpTypeEnum;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import com.carbo.fleet.repository.JobMongoDbRepository;
import com.carbo.fleet.repository.OnSiteEquipmentMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FleetServiceTest {

    @Mock
    private FleetMongoDbRepository fleetRepository;

    @Mock
    private OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    @Mock
    private JobMongoDbRepository jobMongoDbRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FleetService fleetService;

    @Test
    public void shouldReturnAllFleetsWhenGetAllIsCalled() {
        // Arrange
        List<Fleet> mockFleets = Collections.emptyList();
        when(fleetRepository.findAll()).thenReturn(mockFleets);

        // Act
        List<Fleet> result = fleetService.getAll();

        // Assert
        assertEquals(mockFleets, result);
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        // Arrange
        String organizationId = "org123";
        List<Fleet> mockFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(mockFleets);

        // Act
        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(mockFleets, result);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet mockFleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(mockFleet));

        // Act
        Optional<Fleet> result = fleetService.getFleet(fleetId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockFleet, result.get());
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        // Arrange
        Fleet mockFleet = new Fleet();
        when(fleetRepository.save(mockFleet)).thenReturn(mockFleet);

        // Act
        Fleet result = fleetService.saveFleet(mockFleet);

        // Assert
        assertEquals(mockFleet, result);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        // Arrange
        Fleet mockFleet = new Fleet();
        when(fleetRepository.save(mockFleet)).thenReturn(mockFleet);

        // Act
        fleetService.updateFleet(mockFleet);

        // Assert
        Mockito.verify(fleetRepository).save(mockFleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        // Arrange
        String fleetId = "fleet123";

        // Act
        fleetService.deleteFleet(fleetId);

        // Assert
        Mockito.verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        // Arrange
        String organizationId = "org123";
        String fleetName = "FleetName";
        Fleet mockFleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(mockFleet));

        // Act
        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockFleet, result.get());
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        // Arrange
        when(request.getUserPrincipal()).thenReturn(() -> "user123");
        String organizationId = "org123";
        Job mockJob = new Job();
        mockJob.setFleet("FleetName");
        mockJob.setOrganizationId(organizationId);
        List<Job> mockJobs = Collections.singletonList(mockJob);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(mockJobs);

        Fleet mockFleet = new Fleet();
        mockFleet.setName("FleetName");
        mockFleet.setOrganizationId(organizationId);
        List<Fleet> mockFleets = Collections.singletonList(mockFleet);
        when(fleetRepository.findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class))).thenReturn(mockFleets);

        OnSiteEquipment mockEquipment = new OnSiteEquipment();
        mockEquipment.setFleetId("fleetId");
        mockEquipment.setType("pumps");
        mockEquipment.setDuelFuel(true);
        List<OnSiteEquipment> mockEquipmentList = Collections.singletonList(mockEquipment);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any(Set.class))).thenReturn(mockEquipmentList);

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        // Arrange
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Test Exception"));

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody() instanceof Error);
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
