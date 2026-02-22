
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

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnAllFleetsWhenGetAllIsCalled() {
        List<Fleet> fleets = new ArrayList<>();
        Mockito.when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        Mockito.verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = new ArrayList<>();
        Mockito.when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        Mockito.verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        Mockito.verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet fleet = new Fleet();
        fleetService.updateFleet(fleet);

        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet123";
        fleetService.deleteFleet(fleetId);

        Mockito.verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        String organizationId = "org123";
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        
        List<Job> jobList = new ArrayList<>();
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId(organizationId);
        jobList.add(job);

        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(jobList);
        
        Fleet fleet = new Fleet();
        fleet.setId("fleet1Id");
        fleet.setName("Fleet1");
        fleet.setOrganizationId(organizationId);
        
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class)))
                .thenReturn(Collections.singletonList(fleet));
        
        OnSiteEquipment onSiteEquipment = new OnSiteEquipment();
        onSiteEquipment.setFleetId("fleet1Id");
        onSiteEquipment.setType("pumps");
        onSiteEquipment.setDuelFuel(true);
        
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any(Set.class)))
                .thenReturn(Collections.singletonList(onSiteEquipment));
        
        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);
        
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        Mockito.verify(jobMongoDbRepository).findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInGetFleetData() {
        Mockito.when(request.getUserPrincipal()).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        assertEquals(500, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody() instanceof Error);
    }
}
