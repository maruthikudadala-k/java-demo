
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
import static org.mockito.ArgumentMatchers.anyString;

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
    public void shouldReturnAllFleetsWhenGetAllCalled() {
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(1, result.size());
        assertSame(fleets.get(0), result.get(0));
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdCalled() {
        String organizationId = "org1";
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(1, result.size());
        assertSame(fleets.get(0), result.get(0));
    }

    @Test
    public void shouldReturnFleetWhenGetFleetCalled() {
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertTrue(result.isPresent());
        assertSame(fleet, result.get());
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetCalled() {
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertSame(fleet, result);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetCalled() {
        Fleet fleet = new Fleet();
        fleetService.updateFleet(fleet);

        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetCalled() {
        String fleetId = "fleet1";
        fleetService.deleteFleet(fleetId);

        Mockito.verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnFleetWhenFindDistinctByOrganizationIdAndNameCalled() {
        String organizationId = "org1";
        String fleetName = "fleet1";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        assertTrue(result.isPresent());
        assertSame(fleet, result.get());
    }

    @Test
    public void shouldReturnOkResponseWhenGetFleetDataCalled() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenReturn(null); // No principal for simplicity

        Job job = new Job();
        job.setFleet("fleet1");
        job.setOrganizationId("org1");
        List<Job> jobs = Collections.singletonList(job);
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString())).thenReturn(jobs);

        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        fleet.setName("fleet1");
        fleet.setOrganizationId("org1");
        List<Fleet> fleetList = Collections.singletonList(fleet);
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(), any())).thenReturn(fleetList);

        OnSiteEquipment equipment = new OnSiteEquipment();
        equipment.setFleetId("fleetId");
        equipment.setType("pumps");
        equipment.setDuelFuel(true);
        List<OnSiteEquipment> equipmentList = Collections.singletonList(equipment);
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any())).thenReturn(equipmentList);

        ResponseEntity<?> response = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenReturn(null); // No principal for simplicity
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString()))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Error);
        Error error = (Error) response.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
