
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

    @InjectMocks
    private FleetService fleetService;

    @Test
    public void shouldReturnAllFleetsWhenGetAllIsCalled() {
        List<Fleet> mockFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(mockFleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(mockFleets, result);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "orgId";
        List<Fleet> mockFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(mockFleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(mockFleets, result);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleetId";
        Fleet mockFleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(mockFleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertEquals(Optional.of(mockFleet), result);
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet mockFleet = new Fleet();
        when(fleetRepository.save(mockFleet)).thenReturn(mockFleet);

        Fleet result = fleetService.saveFleet(mockFleet);

        assertEquals(mockFleet, result);
        verify(fleetRepository).save(mockFleet);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet mockFleet = new Fleet();

        fleetService.updateFleet(mockFleet);

        verify(fleetRepository).save(mockFleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleetId";

        fleetService.deleteFleet(fleetId);

        verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String organizationId = "orgId";
        Job job = new Job();
        job.setFleet("fleetA");
        job.setOrganizationId(organizationId);
        List<Job> mockJobs = Collections.singletonList(job);

        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(mockJobs);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.emptyList());
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(Collections.emptyList());
        when(request.getUserPrincipal()).thenReturn(null); // Mocking Principal as null for simplicity

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(200, result.getStatusCodeValue());
        verify(jobMongoDbRepository).findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");
        verify(fleetRepository).findByOrganizationIdInAndNameIn(anySet(), anySet());
        verify(onSiteEquipmentMongoDbRepository).findByFleetIdIn(anySet());
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInGetFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(500, result.getStatusCodeValue());
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
