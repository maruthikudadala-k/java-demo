
package com.carbo.fleet.services;

import com.carbo.fleet.events.model.FleetDetails;
import com.carbo.fleet.model.Error;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.model.PumpTypeEnum;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import com.carbo.fleet.repository.JobMongoDbRepository;
import com.carbo.fleet.repository.OnSiteEquipmentMongoDbRepository;
import com.carbo.fleet.model.Fleet;
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
        List<Fleet> fleets = Arrays.asList(new Fleet(), new Fleet());
        Mockito.when(fleetRepository.findAll()).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getAll();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        // Arrange
        String organizationId = "org123";
        List<Fleet> fleets = Arrays.asList(new Fleet(), new Fleet());
        Mockito.when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        // Act
        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        // Assert
        assertEquals(2, result.size());
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
        assertEquals(fleet, result.get());
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        // Act
        Fleet result = fleetService.saveFleet(fleet);

        // Assert
        assertEquals(fleet, result);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        // Arrange
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

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
        String fleetName = "Fleet1";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName))
                .thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String organizationId = "org123";
        Mockito.when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(eq(organizationId), eq("In Progress")))
                .thenReturn(Arrays.asList(new Job(), new Job()));
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(), any())).thenReturn(Arrays.asList(new Fleet(), new Fleet()));
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any())).thenReturn(Arrays.asList(new OnSiteEquipment(), new OnSiteEquipment()));

        // Act
        ResponseEntity<?> result = fleetService.getFleetData(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> result = fleetService.getFleetData(request);

        // Assert
        assertEquals(500, result.getStatusCodeValue());
        Error error = (Error) result.getBody();
        assertNotNull(error);
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
