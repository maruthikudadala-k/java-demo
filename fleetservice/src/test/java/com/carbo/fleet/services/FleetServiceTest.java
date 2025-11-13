
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        List<Fleet> fleets = Collections.emptyList();
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> fleets = Collections.emptyList();
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertEquals(Optional.of(fleet), result);
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
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
    public void shouldReturnFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        String organizationId = "org123";
        String name = "FleetName";
        Fleet fleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertEquals(Optional.of(fleet), result);
        verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String organizationId = "org123";
        when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(Collections.emptyList());
        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(jobMongoDbRepository).findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress");
    }
}
