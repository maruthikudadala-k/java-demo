
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoJUnitRunner.class)
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
    public void shouldReturnAllFleets() {
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findAll()).thenReturn(fleets);

        List<Fleet> result = fleetService.getAll();

        assertEquals(fleets, result);
    }

    @Test
    public void shouldReturnFleetsByOrganizationId() {
        String organizationId = "org123";
        List<Fleet> fleets = Collections.singletonList(new Fleet());
        when(fleetRepository.findByOrganizationId(organizationId)).thenReturn(fleets);

        List<Fleet> result = fleetService.getByOrganizationId(organizationId);

        assertEquals(fleets, result);
    }

    @Test
    public void shouldReturnFleetById() {
        String fleetId = "fleet123";
        Fleet fleet = new Fleet();
        when(fleetRepository.findById(fleetId)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.getFleet(fleetId);

        assertEquals(Optional.of(fleet), result);
    }

    @Test
    public void shouldSaveFleet() {
        Fleet fleet = new Fleet();
        when(fleetRepository.save(fleet)).thenReturn(fleet);

        Fleet result = fleetService.saveFleet(fleet);

        assertEquals(fleet, result);
    }

    @Test
    public void shouldUpdateFleet() {
        Fleet fleet = new Fleet();
        fleetService.updateFleet(fleet);

        Mockito.verify(fleetRepository).save(fleet);
    }

    @Test
    public void shouldDeleteFleetById() {
        String fleetId = "fleet123";
        fleetService.deleteFleet(fleetId);

        Mockito.verify(fleetRepository).deleteById(fleetId);
    }

    @Test
    public void shouldReturnDistinctFleetByOrganizationIdAndName() {
        String organizationId = "org123";
        String fleetName = "Fleet A";
        Fleet fleet = new Fleet();
        when(fleetRepository.findDistinctByOrganizationIdAndName(organizationId, fleetName)).thenReturn(Optional.of(fleet));

        Optional<Fleet> result = fleetService.findDistinctByOrganizationIdAndName(organizationId, fleetName);

        assertEquals(Optional.of(fleet), result);
    }

    @Test
    public void shouldReturnFleetData() {
        String organizationId = "org123";
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> expectedResponse = new HashMap<>();
        Job job = new Job();
        job.setFleet("Fleet A");
        job.setOrganizationId(organizationId);
        List<Job> jobs = Collections.singletonList(job);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress")).thenReturn(jobs);

        Fleet fleet = new Fleet();
        fleet.setName("Fleet A");
        fleet.setOrganizationId(organizationId);
        List<Fleet> fleetList = Collections.singletonList(fleet);
        when(fleetRepository.findByOrganizationIdInAndNameIn(Mockito.anySet(), Mockito.anySet())).thenReturn(fleetList);

        OnSiteEquipment equipment = new OnSiteEquipment();
        equipment.setFleetId(fleet.getId());
        equipment.setType("pumps");
        equipment.setDuelFuel(true);
        List<OnSiteEquipment> equipments = Collections.singletonList(equipment);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(Mockito.anySet())).thenReturn(equipments);

        when(request.getUserPrincipal()).thenReturn(Mockito.mock(Principal.class));
        when(request.getUserPrincipal().getName()).thenReturn(organizationId);

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        // Further assertions on the response data can be added here
    }

    @Test
    public void shouldReturnErrorResponseOnException() {
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity result = fleetService.getFleetData(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Error error = (Error) result.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
