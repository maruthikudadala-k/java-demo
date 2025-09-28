
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
import static org.mockito.ArgumentMatchers.eq;

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
    public void shouldReturnAllFleetsWhenGetAllCalled() {
        // Arrange
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetRepository.findAll()).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getAll();

        // Assert
        assertEquals(fleets, result);
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdCalled() {
        // Arrange
        String organizationId = "org123";
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(fleets, result);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
        // Arrange
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetService.getFleet(fleetId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        // Act
        Fleet result = fleetService.saveFleet(fleet);

        // Assert
        assertEquals(fleet, result);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        // Act
        fleetService.updateFleet(fleet);

        // Assert
        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        // Arrange
        String fleetId = "fleet123";

        // Act
        fleetService.deleteFleet(fleetId);

        // Assert
        Mockito.verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        // Arrange
        String organizationId = "org123";
        String name = "FleetName";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name))
                .thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataCalled() {
        // Arrange
        String organizationId = "org123";
        Job job = new Job();
        job.setFleet("FleetA");
        job.setOrganizationId(organizationId);
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(Collections.singletonList(job));

        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        fleet.setName("FleetA");
        fleet.setOrganizationId(organizationId);
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(), any())).thenReturn(Collections.singletonList(fleet));

        OnSiteEquipment equipment = new OnSiteEquipment();
        equipment.setFleetId("fleetId");
        equipment.setType("pumps");
        equipment.setDuelFuel(true);
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any())).thenReturn(Collections.singletonList(equipment));

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody() instanceof Map);
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        // Arrange
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(), any()))
                .thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity result = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody() instanceof Error);
    }
}
