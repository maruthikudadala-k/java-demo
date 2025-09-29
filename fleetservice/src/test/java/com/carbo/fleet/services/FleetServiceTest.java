
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoJUnitRunner.class)
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
    public void shouldReturnAllFleets() {
        List<Fleet> fleets = Collections.emptyList();
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org123";
        List<Fleet> fleets = Collections.emptyList();
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Optional<Fleet> fleet = Optional.of(new Fleet());
        when(fleetRepository.findById(fleetId)).thenReturn(fleet);

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertEquals(fleet, result);
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
        verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        fleetService.updateFleet(fleet);

        verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet123";
        fleetService.deleteFleet(fleetId);

        verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        String organizationId = "org123";
        String name = "Fleet A";
        Optional<Fleet> fleet = Optional.of(new Fleet());
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(fleet);

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertEquals(fleet, result);
        verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String organizationId = "org123";
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> expectedResponse = new HashMap<>();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = fleetService.getFleetData(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void shouldHandleExceptionInGetFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Error error = (Error) response.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
