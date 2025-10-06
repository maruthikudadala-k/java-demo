
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
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FleetServiceTest {

    @Mock
    private FleetMongoDbRepository fleetRepository;

    @Mock
    private OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    @Mock
    private JobMongoDbRepository jobMongoDbRepository;

    @InjectMocks
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturnAllFleets() {
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        verify(fleetRepository).findAll();
    }

    @Test
    void shouldReturnFleetsByOrganizationId() {
        String organizationId = "orgId";
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    void shouldReturnFleetById() {
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
        verify(fleetRepository).save(fleet);
    }

    @Test
    void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        fleetService.updateFleet(fleet);

        verify(fleetRepository).save(fleet);
    }

    @Test
    void shouldDeleteFleet() {
        String fleetId = "fleetId";
        fleetService.deleteFleet(fleetId);

        verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    void shouldReturnDistinctFleetByOrganizationIdAndName() {
        String organizationId = "orgId";
        String name = "fleetName";
        Fleet fleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    void shouldReturnFleetData() {
        String organizationId = "orgId";
        Job job = new Job();
        job.setFleet("fleetName");
        job.setOrganizationId(organizationId);
        List<Job> jobs = Collections.singletonList(job);
        
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(jobs);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.emptyList());
        
        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(jobMongoDbRepository).findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");
    }

    @Test
    void shouldReturnErrorResponseWhenExceptionOccursInGetFleetData() {
        when(request.getUserPrincipal()).thenThrow(new RuntimeException());

        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof Error);
        Error error = (Error) responseEntity.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
