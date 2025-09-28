
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FleetServiceTest {

    @Mock
    private FleetMongoDbRepository fleetRepository;

    @Mock
    private OnSiteEquipmentMongoDbRepository onSiteEquipmentMongoDbRepository;

    @Mock
    private JobMongoDbRepository jobMongoDbRepository;

    @InjectMocks
    private FleetService fleetService;

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturnFleetDataWhenValidRequest() {
        // Arrange
        String organizationId = "org-123";
        String jobId = "job-1";
        Job job = new Job();
        job.setId(jobId);
        job.setFleet("Fleet A");
        job.setOrganizationId(organizationId);

        List<Job> jobList = Arrays.asList(job);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(jobList);
        
        Fleet fleet = new Fleet();
        fleet.setId("fleet-1");
        fleet.setName("Fleet A");
        fleet.setOrganizationId(organizationId);
        
        when(fleetRepository.findByOrganizationIdInAndNameIn(Set.of(organizationId), Set.of("Fleet A")))
                .thenReturn(Arrays.asList(fleet));

        OnSiteEquipment equipment = new OnSiteEquipment();
        equipment.setFleetId(fleet.getId());
        equipment.setType("pumps");
        equipment.setDuelFuel(true);
        
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(Set.of(fleet.getId())))
                .thenReturn(Collections.singletonList(equipment));

        when(request.getUserPrincipal()).thenReturn(() -> "principal");

        // Act
        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(Map.class, responseEntity.getBody().getClass());
    }

    @Test
    void shouldReturnErrorResponseWhenExceptionOccurs() {
        // Arrange
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(500, responseEntity.getStatusCodeValue());
        Error error = (Error) responseEntity.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
