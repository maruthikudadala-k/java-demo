
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
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        verify(fleetRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        verify(fleetRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
        verify(fleetRepository, times(1)).save(fleet);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet fleet = new Fleet();
        doNothing().when(fleetRepository).save(fleet);

        fleetService.updateFleet(fleet);

        verify(fleetRepository, times(1)).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleetId";
        doNothing().when(fleetRepository).deleteById(fleetId);

        fleetService.deleteFleet(fleetId);

        verify(fleetRepository, times(1)).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        String organizationId = "org123";
        String name = "FleetName";
        Fleet fleet = new Fleet();
        Optional<Fleet> optionalFleet = Optional.of(fleet);
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(optionalFleet);

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertEquals(optionalFleet, result);
        verify(fleetRepository, times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        // Setup mock behavior for the job repository
        String organizationId = "org123";
        Job job = new Job();
        job.setFleet("Fleet1");
        job.setOrganizationId("org123");
        job.setId("job1");
        List<Job> jobList = Collections.singletonList(job);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(jobList);

        // Setup mock behavior for fleet repository
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        fleet.setName("Fleet1");
        fleet.setOrganizationId("org123");
        List<Fleet> fleetLists = Collections.singletonList(fleet);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(fleetLists);

        // Add mock for onSiteEquipment repository
        OnSiteEquipment equipment = new OnSiteEquipment();
        equipment.setFleetId("fleet1");
        equipment.setDuelFuel(true);
        equipment.setType("pumps");
        List<OnSiteEquipment> equipmentList = Collections.singletonList(equipment);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(equipmentList);

        // Call the method under test
        ResponseEntity result = fleetService.getFleetData(request);

        // Validate the result
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void shouldReturnErrorWhenGetFleetDataThrowsException() {
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Error"));

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(500, result.getStatusCodeValue());
        Error expectedError = Error.builder()
                .errorCode("UNABLE_TO_FETCH_DATA")
                .errorMessage("unable to fetch data")
                .build();
        assertEquals(expectedError, result.getBody());
    }
}
