
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.SyncRequest;
import com.carbo.fleet.model.SyncResponse;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private FleetService fleetService;

    @InjectMocks
    private SyncController syncController;

    @Test
    public void shouldReturnFleetTsMapWhenViewIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(new OAuth2AuthenticationStub(organizationId));

        Fleet fleet1 = new Fleet();
        fleet1.setId("fleet1");
        fleet1.setTs(123L);
        
        Fleet fleet2 = new Fleet();
        fleet2.setId("fleet2");
        fleet2.setTs(456L);

        List<Fleet> fleetList = Arrays.asList(fleet1, fleet2);
        when(fleetService.getByOrganizationId(organizationId)).thenReturn(fleetList);

        // Act
        Map<String, Long> result = syncController.view(request);

        // Assert
        Map<String, Long> expected = new HashMap<>();
        expected.put("fleet1", 123L);
        expected.put("fleet2", 456L);
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnSyncResponseWhenSyncIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        request.setUserPrincipal(new OAuth2AuthenticationStub(organizationId));

        SyncRequest syncRequest = new SyncRequest();
        Set<String> remove = new HashSet<>(Collections.singletonList("fleet1"));
        syncRequest.setRemove(remove);

        Fleet fleetToUpdate = new Fleet();
        fleetToUpdate.setId("fleet2");
        fleetToUpdate.setOrganizationId(organizationId);
        fleetToUpdate.setTs(1000L);
        syncRequest.setUpdate(Collections.singletonList(fleetToUpdate));

        Fleet existingFleet = new Fleet();
        existingFleet.setId("fleet2");
        existingFleet.setTs(500L);
        
        when(fleetService.getFleet("fleet2")).thenReturn(Optional.of(existingFleet));
        when(fleetService.deleteFleet("fleet1")).thenReturn(null);
        
        // Act
        SyncResponse response = syncController.sync(request, syncRequest);

        // Assert
        assertEquals(1, response.getRemoved().size());
        assertEquals("fleet1", response.getRemoved().iterator().next());
        assertEquals(0, response.getUpdated().size());
    }

    // Add more tests as necessary for the remaining methods

    private static class OAuth2AuthenticationStub extends OAuth2Authentication {
        private final String organizationId;

        public OAuth2AuthenticationStub(String organizationId) {
            super(null, null);
            this.organizationId = organizationId;
        }

        @Override
        public Object getDetails() {
            Map<String, String> details = new HashMap<>();
            details.put("organizationId", organizationId);
            return details;
        }
    }
}
