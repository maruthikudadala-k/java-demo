
package com.carbo.fleet.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.carbo.fleet.events.model.FleetDetails;
import com.carbo.fleet.model.Error;
import com.carbo.fleet.model.Job;
import com.carbo.fleet.model.OnSiteEquipment;
import com.carbo.fleet.model.PumpTypeEnum;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import com.carbo.fleet.repository.JobMongoDbRepository;
import com.carbo.fleet.repository.OnSiteEquipmentMongoDbRepository;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public void shouldReturnFleetDataWhenValidRequest() {
        // Arrange
        String organizationId = "org123";
        List<Job> jobList = new ArrayList<>();
        Job job1 = new Job();
        job1.setId("job1");
        job1.setFleet("Fleet1");
        job1.setOrganizationId(organizationId);
        jobList.add(job1);

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setName("Fleet1");
        fleet1.setOrganizationId(organizationId);

        List<Fleet> fleetLists = Collections.singletonList(fleet1);
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(organizationId, "In Progress"))
                .thenReturn(jobList);
        when(fleetRepository.findByOrganizationIdInAndNameIn(anySet(), anySet()))
                .thenReturn(fleetLists);
        when(onSiteEquipmentMongoDbRepository.findByFleetIdIn(anySet())).thenReturn(Collections.emptyList());
        when(request.getUserPrincipal()).thenReturn(() -> organizationId);

        // Act
        ResponseEntity<?> response = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new HashMap<>(), response.getBody());
    }

    @Test
    public void shouldReturnErrorResponseWhenExceptionOccurs() {
        // Arrange
        when(jobMongoDbRepository.findBySharedWithOrganizationIdAndStatus(anyString(), anyString()))
                .thenThrow(new RuntimeException("Test Exception"));

        // Act
        ResponseEntity<?> response = fleetService.getFleetData(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Error error = (Error) response.getBody();
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_CODE, error.getErrorCode());
        assertEquals(Constants.UNABLE_TO_FETCH_DATA_MESSAGE, error.getErrorMessage());
    }
}
