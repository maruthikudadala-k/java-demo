
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
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        List<Fleet> expectedFleets = Collections.emptyList();
        when(fleetRepository.findAll()).thenReturn(expectedFleets);
        
        List<Fleet> actualFleets = fleetService.getAll();
        
        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(expectedFleets);
        
        List<Fleet> actualFleets = fleetService.getByOrganizationId(organizationId);
        
        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet expectedFleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(expectedFleet));
        
        Optional<Fleet> actualFleet = fleetService.getFleet(fleetId);
        
        assertTrue(actualFleet.isPresent());
        assertEquals(expectedFleet, actualFleet.get());
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);
        
        Fleet actualFleet = fleetService.saveFleet(fleet);
        
        assertEquals(fleet, actualFleet);
        verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet fleet = new Fleet();
        
        fleetService.updateFleet(fleet);
        
        verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet123";
        
        fleetService.deleteFleet(fleetId);
        
        verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String organizationId = "org123";
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);
        List<Job> jobList = Collections.singletonList(job);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(jobList);

        Fleet fleet = new Fleet();
        fleet.setName("Fleet1");
        fleet.setOrganizationId(organizationId);
        List<Fleet> fleetLists = Collections.singletonList(fleet);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(fleetLists);

        OnSiteEquipment onSiteEquipment = new OnSiteEquipment();
        onSiteEquipment.setFleetId(fleet.getId());
        onSiteEquipment.setType("pumps");
        onSiteEquipment.setDuelFuel(true);
        List<OnSiteEquipment> data = Collections.singletonList(onSiteEquipment);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(data);

        ResponseEntity<?> response = fleetService.getFleetData(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = fleetService.getFleetData(request);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Error);
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, ((Error) response.getBody()).getErrorCode());
    }
}
