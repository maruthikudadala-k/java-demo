
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
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), anyString(), eq(PersonnelDto.class))).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.emptyList(), null));
        when(mongoTemplate.aggregate(any(), anyString(), eq(TotalCountObject.class))).thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(new TotalCountObject(0L)), null));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
        verify(mongoTemplate, times(1)).aggregate(any(), anyString(), eq(PersonnelDto.class));
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        Personnel createdPersonnel = Personnel.builder()
                .id("newId")
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(createdPersonnel);

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(2)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnFalseWhenSavePersonnelWithDuplicateKey() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.save(any(Personnel.class))).thenThrow(new DuplicateKeyException("Duplicate key"));

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertFalse(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "personnelId";
        PersonnelDto expectedDto = PersonnelDto.builder().build();
        PersonnelDisplay display = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();

        when(personnelService.lookUpPersonnel(any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(display);

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    public void shouldDeletePersonnelSuccessfully() {
        // Arrange
        String id = "personnelId";
        Personnel personnel = Personnel.builder().id(id).build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldNotDeletePersonnelWhenNotFound() {
        // Arrange
        String id = "nonExistingId";

        when(personnelDBRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, never()).deleteById(anyString());
    }
}
