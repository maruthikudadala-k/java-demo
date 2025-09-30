
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
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        List<Fleet> mockFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(mockFleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(mockFleets, result);
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org123";
        List<Fleet> mockFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(mockFleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(mockFleets, result);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet mockFleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(mockFleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertEquals(Optional.of(mockFleet), result);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet mockFleet = new Fleet();
        when(fleetRepository.save(mockFleet)).thenReturn(mockFleet);

        Fleet result = fleetService.saveFleet(mockFleet);

        assertEquals(mockFleet, result);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet mockFleet = new Fleet();
        fleetService.updateFleet(mockFleet);

        verify(fleetRepository).save(mockFleet);
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
        Fleet mockFleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(mockFleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        assertEquals(Optional.of(mockFleet), result);
    }

    @Test
    public void shouldGetFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenReturn(() -> "principal");
        Job mockJob = new Job();
        mockJob.setFleet("Fleet A");
        mockJob.setOrganizationId("org123");
        List<Job> mockJobs = Collections.singletonList(mockJob);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus("org123", "In Progress")).thenReturn(mockJobs);

        Fleet mockFleet = new Fleet();
        mockFleet.setId("fleet123");
        mockFleet.setName("Fleet A");
        mockFleet.setOrganizationId("org123");
        List<Fleet> mockFleets = Collections.singletonList(mockFleet);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(mockFleets);
        
        OnSiteEquipment mockEquipment = new OnSiteEquipment();
        mockEquipment.setFleetId("fleet123");
        mockEquipment.setType("pumps");
        mockEquipment.setDuelFuel(true);
        List<OnSiteEquipment> mockEquipmentList = Collections.singletonList(mockEquipment);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(mockEquipmentList);

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody() instanceof Map);
    }

    @Test
    public void shouldHandleExceptionInGetFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenThrow(new RuntimeException());

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertTrue(result.getBody() instanceof Error);
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
