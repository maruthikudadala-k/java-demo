
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
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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

    @Mock
    private HttpServletRequest request;

    @Test
    public void shouldReturnOkResponseWithFleetDataWhenGetFleetDataIsCalled() {
        // Arrange
        String organizationId = "orgId";
        Job job1 = new Job();
        job1.setId("job1");
        job1.setFleet("Fleet1");
        job1.setOrganizationId(organizationId);

        Job job2 = new Job();
        job2.setId("job2");
        job2.setFleet("Fleet2");
        job2.setOrganizationId(organizationId);

        List<Job> jobList = Arrays.asList(job1, job2);

        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(eq(organizationId), eq("In Progress")))
                .thenReturn(jobList);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setName("Fleet1");
        fleet1.setOrganizationId(organizationId);

        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setName("Fleet2");
        fleet2.setOrganizationId(organizationId);

        List<Fleet> fleetLists = Arrays.asList(fleet1, fleet2);
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(), any()))
                .thenReturn(fleetLists);

        OnSiteEquipment equipment1 = new OnSiteEquipment();
        equipment1.setFleetId("fleet1");
        equipment1.setType("pumps");
        equipment1.setDuelFuel(true);

        OnSiteEquipment equipment2 = new OnSiteEquipment();
        equipment2.setFleetId("fleet1");
        equipment2.setType("epumps");
        equipment2.setDuelFuel(false);

        List<OnSiteEquipment> equipmentList = Arrays.asList(equipment1, equipment2);
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any()))
                .thenReturn(equipmentList);

        Mockito.when(request.getUserPrincipal()).thenReturn(() -> organizationId);

        // Act
        ResponseEntity responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> responseBody = (Map<String, Map<String, Map<PumpTypeEnum, Integer>>>) responseEntity.getBody();
        assertEquals(1, responseBody.size());
        assertEquals(2, responseBody.get(organizationId).size());
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccursInGetFleetData() {
        // Arrange
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(any(), any()))
                .thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(500, responseEntity.getStatusCodeValue());
        Error error = (Error) responseEntity.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
