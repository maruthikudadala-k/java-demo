
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
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyString;
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

    @BeforeEach
    public void setUp() {
        // Setup mock data if needed
    }

    @Test
    public void shouldReturnAllFleets() {
        List<Fleet> fleets = new ArrayList<>();
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        Mockito.verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org1";
        List<Fleet> fleets = new ArrayList<>();
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        Mockito.verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet1";
        Fleet fleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        Mockito.verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();

        fleetService.updateFleet(fleet);

        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet1";

        fleetService.deleteFleet(fleetId);

        Mockito.verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetByOrganizationIdAndName() {
        String organizationId = "org1";
        String fleetName = "Fleet A";
        Fleet fleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName))
                .thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        Mockito.verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, fleetName);
    }

    @Test
    public void shouldReturnFleetData() {
        when(request.getUserPrincipal()).thenReturn(() -> "user");
        
        String organizationId = "org1";
        Job job1 = new Job();
        job1.setFleet("Fleet A");
        job1.setOrganizationId(organizationId);
        job1.setId("job1");

        Job job2 = new Job();
        job2.setFleet("Fleet B");
        job2.setOrganizationId(organizationId);
        job2.setId("job2");

        List<Job> jobList = Arrays.asList(job1, job2);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(jobList);

        Fleet fleetA = new Fleet();
        fleetA.setId("fleetA");
        fleetA.setName("Fleet A");
        fleetA.setOrganizationId(organizationId);

        Fleet fleetB = new Fleet();
        fleetB.setId("fleetB");
        fleetB.setName("Fleet B");
        fleetB.setOrganizationId(organizationId);

        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet()))
                .thenReturn(Arrays.asList(fleetA, fleetB));

        OnSiteEquipment equipment = new OnSiteEquipment();
        equipment.setFleetId(fleetA.getId());
        equipment.setType("pumps");
        equipment.setDuelFuel(true);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet()))
                .thenReturn(Collections.singletonList(equipment));

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void shouldReturnErrorResponseOnException() {
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Error"));

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody() instanceof Error);
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
