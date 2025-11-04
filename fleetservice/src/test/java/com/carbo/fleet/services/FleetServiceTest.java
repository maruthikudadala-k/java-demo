
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;

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
        List<Fleet> fleets = new ArrayList<>();
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
        verify(fleetRepository).findAll();
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org123";
        List<Fleet> fleets = new ArrayList<>();
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
        verify(fleetRepository).findByOrganizationId(organizationId);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        verify(fleetRepository).findById(fleetId);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
        verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        doNothing().when(fleetRepository).save(fleet);

        fleetService.updateFleet(fleet);

        verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleet() {
        String fleetId = "fleet123";
        doNothing().when(fleetRepository).deleteById(fleetId);

        fleetService.deleteFleet(fleetId);

        verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldFindDistinctFleetByOrgIdAndName() {
        String organizationId = "org123";
        String name = "Fleet A";
        Fleet fleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, name)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, name);

        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        verify(fleetRepository).findDistinctByOrganizationIdAndName(organizationId, name);
    }
}
