
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
        List<Fleet> expectedFleets = new ArrayList<>();
        when(fleetRepository.findAll()).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getAll();

        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> expectedFleets = new ArrayList<>();
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

        assertEquals(Optional.of(expectedFleet), actualFleet);
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
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> expectedData = new HashMap<>();
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(new ArrayList<>());

        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedData, responseEntity.getBody());
    }

    @Test
    public void shouldReturnErrorWhenGetFleetDataThrowsException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString())).thenThrow(new RuntimeException());

        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Error error = (Error) responseEntity.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
