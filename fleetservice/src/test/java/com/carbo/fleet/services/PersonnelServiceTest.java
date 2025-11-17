
package com.carbo.fleet.services;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.Personnel;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.model.TotalCountObject;
import com.carbo.fleet.repository.PersonnelDBRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonnelServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private PersonnelDBRepository personnelDBRepository;

    @InjectMocks
    private PersonnelService personnelService;

    @Test
    public void shouldReturnPersonnelDisplayWhenFindAll() {
        // Arrange
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelDBRepository.findByOrganizationId(organizationId, PageRequest.of(offSet / limit, limit)))
                .thenReturn(Optional.of(Collections.emptyList()));
        when(mongoTemplate.aggregate(any(), anyString(), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.emptyList(), null));

        // Act
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelDBRepository, times(1)).findByOrganizationId(organizationId, PageRequest.of(offSet / limit, limit));
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Engineer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        Personnel newPersonnel = Personnel.builder()
                .crewId(dto.getCrewId())
                .employeeId(dto.getEmployeeId())
                .firstName(dto.getFirstName())
                .districtId(dto.getDistrictId())
                .jobTitle(dto.getJobTitle())
                .fleetId(dto.getFleetId())
                .secondName(dto.getSecondName())
                .supervisor(dto.getSupervisor())
                .organizationId(dto.getOrganizationId())
                .build();

        when(personnelDBRepository.save(newPersonnel)).thenReturn(newPersonnel);

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(newPersonnel);
    }

    @Test
    public void shouldUpdatePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("personnel123")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Engineer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisor123")
                .organizationId("org123")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .build();

        when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "personnel123";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .build();
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();

        when(personnelService.lookUpPersonnel(any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(personnelDisplay);

        // Act
        PersonnelDto actualDto = personnelService.findById(id);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelService, times(1)).lookUpPersonnel(any(), any(), any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    public void shouldDeletePersonnelWhenExists() {
        // Arrange
        String id = "personnel123";
        Personnel personnel = Personnel.builder()
                .id(id)
                .build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }
}
