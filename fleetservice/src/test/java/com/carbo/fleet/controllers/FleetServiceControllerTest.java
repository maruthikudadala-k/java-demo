
package com.carbo.fleet.controllers;

import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.services.FleetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoJUnitRunner.class)
public class FleetServiceControllerTest {

    @Mock
    private FleetService fleetService;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private FleetServiceController fleetServiceController;

    @Test
    public void shouldReturnAllFleetsWhenOperator() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId("orgId")).thenReturn(Collections.emptyList());

        // Act
        List<Fleet> result = fleetServiceController.getFleets(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService).getByOrganizationId("orgId");
    }

    @Test
    public void shouldReturnFleetWhenFleetExists() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        when(fleetService.getFleet(fleetId)).thenReturn(Optional.of(fleet));

        // Act
        Fleet result = fleetServiceController.getFleet(fleetId);

        // Assert
        assertNotNull(result);
        assertEquals(fleet, result);
        verify(fleetService).getFleet(fleetId);
    }

    @Test
    public void shouldUpdateFleetSuccessfully() {
        // Arrange
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();

        // Act
        fleetServiceController.updateFleet(fleetId, fleet);

        // Assert
        verify(fleetService).updateFleet(fleet);
    }

    @Test
    public void shouldSaveFleetSuccessfully() {
        // Arrange
        Fleet fleet = new Fleet();

        // Act
        fleetServiceController.saveFleet(fleet);

        // Assert
        verify(fleetService).saveFleet(fleet);
    }

    @Test
    public void shouldDeleteFleetSuccessfully() {
        // Arrange
        String fleetId = "fleetId";

        // Act
        fleetServiceController.deleteFleet(fleetId);

        // Assert
        verify(fleetService).deleteFleet(fleetId);
    }

    @Test
    public void shouldFindDistinctByOrganizationIdAndName() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String name = "fleetName";
        Fleet fleet = new Fleet();
        when(fleetService.findDistinctByOrganizationIdAndName("orgId", name)).thenReturn(Optional.of(fleet));

        // Act
        Optional<Fleet> result = fleetServiceController.findDistinctByOrganizationIdAndName(request, name);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fleet, result.get());
        verify(fleetService).findDistinctByOrganizationIdAndName("orgId", name);
    }

    @Test
    public void shouldGetFleetDataSuccessfully() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getFleetData(request)).thenReturn(ResponseEntity.ok().build());

        // Act
        ResponseEntity result = fleetServiceController.getFleetData(request);

        // Assert
        assertNotNull(result);
        verify(fleetService).getFleetData(request);
    }

    @Test
    public void shouldReturnFleetsForCalendarWhenOperator() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(fleetService.getByOrganizationId("orgId")).thenReturn(Collections.emptyList());

        // Act
        List<Fleet> result = fleetServiceController.getFleetsForCalendar(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(fleetService).getByOrganizationId("orgId");
    }
}
