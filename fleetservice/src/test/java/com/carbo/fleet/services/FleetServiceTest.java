
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

import static org.junit.jupiter.api.Assertions.*;
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
    public void shouldReturnAllFleetsWhenGetAllIsCalled() {
        // Arrange
        List<Fleet> fleets = new ArrayList<>();
        Mockito.when(fleetRepository.findAll()).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getAll();

        // Assert
        assertSame(fleets, result);
        Mockito.verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        // Arrange
        String organizationId = "org123";
        List<Fleet> fleets = new ArrayList<>();
        Mockito.when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        // Assert
        assertSame(fleets, result);
        Mockito.verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetService.getFleet(fleetId);

        // Assert
        assertTrue(result.isPresent());
        assertSame(fleet, result.get());
        Mockito.verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        // Act
        Fleet result = fleetService.saveFleet(fleet);

        // Assert
        assertSame(fleet, result);
        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        // Arrange
        Fleet fleet = new Fleet();

        // Act
        fleetService.updateFleet(fleet);

        // Assert
        Mockito.verify(fleetRepository).save(fleet);
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
    public void shouldReturnFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        // Arrange
        String organizationId = "org123";
        String fleetName = "Fleet A";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName))
                .thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        // Assert
        assertTrue(result.isPresent());
        assertSame(fleet, result.get());
        Mockito.verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, fleetName);
    }

    @Test
    public void shouldReturnOkResponseWhenGetFleetDataIsCalled() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String organizationId = "org123";
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> expectedResponse = new HashMap<>();
        Job job = new Job();
        job.setFleet("Fleet A");
        job.setOrganizationId(organizationId);
        List<Job> jobList = Collections.singletonList(job);
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(jobList);
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class)))
                .thenReturn(new ArrayList<>());
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any(Set.class))).thenReturn(new ArrayList<>());
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(((Map) any()).get("organizationId")).thenReturn(organizationId);

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedResponse, result.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(), any())).thenThrow(new RuntimeException());

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(500, result.getStatusCodeValue());
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
