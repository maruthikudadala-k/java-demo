
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

        assertTrue(actualFleet.isPresent());
        assertEquals(expectedFleet, actualFleet.get());
        verify(fleetRepository, times(1)).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleetToSave = new Fleet();
        Fleet expectedFleet = new Fleet();
        when(fleetRepository.save(fleetToSave)).thenReturn(expectedFleet);

        Fleet actualFleet = fleetService.saveFleet(fleetToSave);

        assertEquals(expectedFleet, actualFleet);
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
    public void shouldReturnDistinctFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        String organizationId = "org123";
        String name = "fleetName";
        Fleet expectedFleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(expectedFleet));

        Optional<Fleet> actualFleet = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertTrue(actualFleet.isPresent());
        assertEquals(expectedFleet, actualFleet.get());
        verify(fleetRepository, times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnOkResponseWhenGetFleetDataIsCalled() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString())).thenReturn(Collections.emptyList());
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet())).thenReturn(Collections.emptyList());
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(Collections.emptyList());

        ResponseEntity response = fleetService.getFleetData(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(jobMongoDbRepository, times(1)).findBySharedWithOrganizationIdAndStatus(anyString(), anyString());
        verify(fleetRepository, times(1)).findByOrganizationIdInAndNameIn(anySet(), anySet());
        verify(onSiteEquipmentMongoDbRepository, times(1)).findByFleetIdIn(anySet());
    }

    @Test
    public void shouldReturnErrorResponseWhenGetFleetDataThrowsException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getUserPrincipal()).thenThrow(new RuntimeException("Test Exception"));

        ResponseEntity response = fleetService.getFleetData(request);

        assertEquals(500, response.getStatusCodeValue());
        Error error = (Error) response.getBody();
        assertNotNull(error);
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
