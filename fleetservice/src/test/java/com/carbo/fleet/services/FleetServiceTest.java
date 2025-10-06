
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
    public void shouldReturnAllFleets() {
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getAll();

        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org123";
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getByOrganizationId(organizationId);

        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet expectedFleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(expectedFleet));

        Optional<Fleet> actualFleet = fleetService.getFleet(fleetId);

        assertTrue(actualFleet.isPresent());
        assertEquals(expectedFleet, actualFleet.get());
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleetToSave = new Fleet();
        Fleet savedFleet = new Fleet();
        when(fleetRepository.save(fleetToSave)).thenReturn(savedFleet);

        Fleet actualFleet = fleetService.saveFleet(fleetToSave);

        assertEquals(savedFleet, actualFleet);
        verify(fleetRepository).save(fleetToSave);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleetToUpdate = new Fleet();

        fleetService.updateFleet(fleetToUpdate);

        verify(fleetRepository).save(fleetToUpdate);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet123";

        fleetService.deleteFleet(fleetId);

        verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetByOrganizationIdAndName() {
        String organizationId = "org123";
        String fleetName = "Fleet A";
        Fleet expectedFleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(expectedFleet));

        Optional<Fleet> actualFleet = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        assertTrue(actualFleet.isPresent());
        assertEquals(expectedFleet, actualFleet.get());
        verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, fleetName);
    }

    @Test
    public void shouldReturnFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String organizationId = "org123";
        Job job = new Job();
        job.setFleet("Fleet A");
        job.setOrganizationId(organizationId);
        job.setId("job1");

        List<Job> jobList = Collections.singletonList(job);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(jobList);

        Set<String> filterFleetNames = new HashSet<>(Collections.singletonList("Fleet A"));
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.emptyList());

        ResponseEntity responseEntity = fleetService.getFleetData(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(jobMongoDbRepository).findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");
    }
    
    @Test
    public void shouldReturnErrorOnException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString())).thenThrow(new RuntimeException("Error"));

        ResponseEntity responseEntity = fleetService.getFleetData(request);

        assertEquals(500, responseEntity.getStatusCodeValue());
        Error error = (Error) responseEntity.getBody();
        assertNotNull(error);
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
