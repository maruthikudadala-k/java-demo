
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

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private FleetService fleetService;

    @Test
    public void shouldReturnAllFleetsWhenGetAllIsCalled() {
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getAll();

        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getByOrganizationId(organizationId);

        assertEquals(expectedFleets, actualFleets);
        verify(fleetRepository, times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet expectedFleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(expectedFleet));

        Optional<Fleet> actualFleet = fleetService.getFleet(fleetId);

        assertEquals(expectedFleet, actualFleet.get());
        verify(fleetRepository, times(1)).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleetToSave = new Fleet();
        Fleet savedFleet = new Fleet();
        when(fleetRepository.save(fleetToSave)).thenReturn(savedFleet);

        Fleet actualFleet = fleetService.saveFleet(fleetToSave);

        assertEquals(savedFleet, actualFleet);
        verify(fleetRepository, times(1)).save(fleetToSave);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet fleetToUpdate = new Fleet();

        fleetService.updateFleet(fleetToUpdate);

        verify(fleetRepository, times(1)).save(fleetToUpdate);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet123";

        fleetService.deleteFleet(fleetId);

        verify(fleetRepository, times(1)).deleteById(fleetId);
    }

    @Test
    public void shouldReturnResponseEntityWhenGetFleetDataIsCalled() {
        String organizationId = "org123";
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> expectedResponse = new HashMap<>();
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(Collections.emptyList());
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.emptyList());
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(Collections.emptyList());

        ResponseEntity<?> actualResponse = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
    }

    @Test
    public void shouldReturnErrorResponseEntityWhenGetFleetDataThrowsException() {
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity<?> actualResponse = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, ((Error) actualResponse.getBody()).getErrorMessage());
    }
}
