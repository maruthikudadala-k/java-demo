
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoJUnitRunner.class)
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
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetRepository.findAll()).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getAll();
        
        assertEquals(expectedFleets, actualFleets);
        Mockito.verify(fleetRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationIdWhenGetByOrganizationIdIsCalled() {
        String organizationId = "org123";
        List<Fleet> expectedFleets = Collections.singletonList(new Fleet());
        Mockito.when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(expectedFleets);

        List<Fleet> actualFleets = fleetService.getByOrganizationId(organizationId);
        
        assertEquals(expectedFleets, actualFleets);
        Mockito.verify(fleetRepository, Mockito.times(1)).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetWhenGetFleetIsCalled() {
        String fleetId = "fleet123";
        Fleet expectedFleet = new Fleet();
        Mockito.when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(expectedFleet));

        Optional<Fleet> actualFleet = fleetService.getFleet(fleetId);
        
        assertEquals(Optional.of(expectedFleet), actualFleet);
        Mockito.verify(fleetRepository, Mockito.times(1)).findById(fleetId);
    }

    @Test
    public void shouldSaveFleetWhenSaveFleetIsCalled() {
        Fleet fleet = new Fleet();
        Mockito.when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet savedFleet = fleetService.saveFleet(fleet);
        
        assertEquals(fleet, savedFleet);
        Mockito.verify(fleetRepository, Mockito.times(1)).save(fleet);
    }

    @Test
    public void shouldUpdateFleetWhenUpdateFleetIsCalled() {
        Fleet fleet = new Fleet();
        fleetService.updateFleet(fleet);
        
        Mockito.verify(fleetRepository, Mockito.times(1)).save(fleet);
    }

    @Test
    public void shouldDeleteFleetWhenDeleteFleetIsCalled() {
        String fleetId = "fleet123";
        fleetService.deleteFleet(fleetId);
        
        Mockito.verify(fleetRepository, Mockito.times(1)).deleteById(fleetId);
    }

    @Test
    public void shouldReturnFleetWhenFindDistinctByOrganizationIdAndNameIsCalled() {
        String organizationId = "org123";
        String name = "Fleet A";
        Fleet expectedFleet = new Fleet();
        Mockito.when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(expectedFleet));

        Optional<Fleet> actualFleet = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);
        
        assertEquals(Optional.of(expectedFleet), actualFleet);
        Mockito.verify(fleetRepository, Mockito.times(1)).findDistinctByOrganizationIdAndName(organizationId, name);
    }

    @Test
    public void shouldReturnFleetDataWhenGetFleetDataIsCalled() {
        HttpServletRequest request = new MockHttpServletRequest();
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(String.class), any(String.class)))
                .thenReturn(Collections.singletonList(new Job()));
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class)))
                .thenReturn(Collections.singletonList(new Fleet()));
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any(Set.class)))
                .thenReturn(Collections.singletonList(new OnSiteEquipment()));

        ResponseEntity<?> response = fleetService.getFleetData(request);
        
        assertEquals(200, response.getStatusCodeValue());
        Mockito.verify(jobMongoDbRepository, Mockito.times(1)).findBySharedWithOrganizationIdAndStatus(any(String.class), any(String.class));
        Mockito.verify(fleetRepository, Mockito.times(1)).findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class));
        Mockito.verify(onSiteEquipmentMongoDbRepository, Mockito.times(1)).findByFleetIdIn(any(Set.class));
    }
}
