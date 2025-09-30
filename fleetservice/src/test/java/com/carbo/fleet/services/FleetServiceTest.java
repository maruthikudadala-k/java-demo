
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
        Fleet fleet1 = new Fleet();
        Fleet fleet2 = new Fleet();
        List<Fleet> fleets = Arrays.asList(fleet1, fleet2);

        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(2, result.size());
        verify(fleetRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org123";
        Fleet fleet = new Fleet();
        List<Fleet> fleets = Collections.singletonList(fleet);

        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(1, result.size());
        verify(fleetRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();

        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertEquals(fleet, result.get());
        verify(fleetRepository, times(1)).findById(fleetId);
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

        fleetService.updateFleet(fleet);

        verify(fleetRepository, times(1)).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet123";

        fleetService.deleteFleet(fleetId);

        verify(fleetRepository, times(1)).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetByOrganizationIdAndName() {
        String organizationId = "org123";
        String name = "Fleet A";
        Fleet fleet = new Fleet();

        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertEquals(fleet, result.get());
        verify(fleetRepository, times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String organizationId = "org123";
        Job job = new Job();
        job.setFleet("Fleet A");
        job.setOrganizationId(organizationId);
        List<Job> jobList = Collections.singletonList(job);

        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(jobList);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.emptyList());
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(Collections.emptyList());

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(200, result.getStatusCodeValue());
        verify(jobMongoDbRepository, times(1)).findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInGetFleetData() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(500, result.getStatusCodeValue());
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
