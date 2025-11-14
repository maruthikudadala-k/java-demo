
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

    @Test
    public void shouldReturnFleetDataWhenValidRequest() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenReturn(() -> "mockPrincipal");

        Job job1 = new Job();
        job1.setId("job1");
        job1.setFleet("Fleet1");
        job1.setOrganizationId("Org1");

        Job job2 = new Job();
        job2.setId("job2");
        job2.setFleet("Fleet2");
        job2.setOrganizationId("Org1");

        List<Job> jobList = Arrays.asList(job1, job2);
        Mockito.when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(eq("Org1"), eq("In Progress")))
                .thenReturn(jobList);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setName("Fleet1");
        fleet1.setOrganizationId("Org1");

        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setName("Fleet2");
        fleet2.setOrganizationId("Org1");

        List<Fleet> fleetLists = Arrays.asList(fleet1, fleet2);
        Mockito.when(fleetRepository.findByOrganizationIdInAndNameIn(any(Set.class), any(Set.class)))
                .thenReturn(fleetLists);

        OnSiteEquipment equipment1 = new OnSiteEquipment();
        equipment1.setFleetId("fleet1");
        equipment1.setType("pumps");
        equipment1.setDuelFuel(true);

        OnSiteEquipment equipment2 = new OnSiteEquipment();
        equipment2.setFleetId("fleet1");
        equipment2.setType("epumps");

        List<OnSiteEquipment> equipmentList = Arrays.asList(equipment1, equipment2);
        Mockito.when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(any(Set.class)))
                .thenReturn(equipmentList);

        // Act
        ResponseEntity responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Map<String, Map<PumpTypeEnum, Integer>>> responseBody = (Map<String, Map<String, Map<PumpTypeEnum, Integer>>>) responseEntity.getBody();
        assertEquals(1, responseBody.get("Org1").get("Fleet1").get(PumpTypeEnum.DUAL).intValue());
        assertEquals(1, responseBody.get("Org1").get("Fleet1").get(PumpTypeEnum.ELECTRIC).intValue());
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccurs() {
        // Arrange
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getUserPrincipal()).thenThrow(new RuntimeException("Mock Exception"));

        // Act
        ResponseEntity responseEntity = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Error error = (Error) responseEntity.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
