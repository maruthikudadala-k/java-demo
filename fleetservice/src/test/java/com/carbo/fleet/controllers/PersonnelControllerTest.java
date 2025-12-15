
package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import com.carbo.fleet.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PersonnelControllerTest {

    @Mock
    private PersonnelService personnelService;

    @InjectMocks
    private PersonnelController personnelController;

    @Test
    public void shouldReturnAllPersonnelWhenGetAllPersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findAll(organizationId, offSet, limit)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnel(request, offSet, limit);

        // Assert
        assertThat(actualDisplay).isEqualTo(expectedDisplay);
        Mockito.verify(personnelService).findAll(organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnPersonnelWhenGetPersonnelIsCalled() {
        // Arrange
        String personnelId = "personnelId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(personnelId)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("districtId")
                .fleetId("fleetId")
                .crewId("crewId")
                .build();

        Mockito.when(personnelService.findById(personnelId)).thenReturn(expectedDto);

        // Act
        PersonnelDto actualDto = personnelController.getPersonnel(new MockHttpServletRequest(), personnelId);

        // Assert
        assertThat(actualDto).isEqualTo(expectedDto);
        Mockito.verify(personnelService).findById(personnelId);
    }

    @Test
    public void shouldCreatePersonnelWhenCreatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("districtId")
                .fleetId("fleetId")
                .crewId("crewId")
                .build();
        personnelDto.setOrganizationId(organizationId);
        Mockito.when(personnelService.savePersonnel(personnelDto)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = personnelController.createPersonnel(request, personnelDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isInstanceOf(java.util.HashMap.class);
        Mockito.verify(personnelService).savePersonnel(personnelDto);
    }

    @Test
    public void shouldUpdatePersonnelWhenUpdatePersonnelIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("personnelId")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("districtId")
                .fleetId("fleetId")
                .crewId("crewId")
                .build();
        personnelDto.setOrganizationId(organizationId);
        Mockito.when(personnelService.updatePersonnel(personnelDto)).thenReturn(true);
        Mockito.when(personnelService.findById(personnelDto.getId())).thenReturn(personnelDto);

        // Act
        PersonnelDto actualDto = personnelController.updatePersonnel(request, personnelDto);

        // Assert
        assertThat(actualDto).isEqualTo(personnelDto);
        Mockito.verify(personnelService).updatePersonnel(personnelDto);
        Mockito.verify(personnelService).findById(personnelDto.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalled() {
        // Arrange
        String personnelId = "personnelId";

        // Act
        personnelController.deletePersonnel(personnelId);

        // Assert
        Mockito.verify(personnelService).deletePersonnel(personnelId);
    }

    @Test
    public void shouldReturnPersonnelByFilterWhenGetAllPersonnelByFilterIsCalled() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        String personnelName = "John";
        String districtId = "districtId";
        String jobTitle = "Engineer";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit)).thenReturn(expectedDisplay);

        // Act
        PersonnelDisplay actualDisplay = personnelController.getAllPersonnelByFilter(request, offSet, limit, personnelName, districtId, jobTitle);

        // Assert
        assertThat(actualDisplay).isEqualTo(expectedDisplay);
        Mockito.verify(personnelService).findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
