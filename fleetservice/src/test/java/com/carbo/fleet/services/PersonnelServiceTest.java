
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
import org.mockito.junit.MockitoExtension;
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

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.emptyList(), null));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(TotalCountObject.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(new TotalCountObject(0L)), null));

        // Act
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, result);
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
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

        Personnel savedPersonnel = Personnel.builder()
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(savedPersonnel);

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("personnelId")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org123")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .crewId("oldCrewId")
                .employeeId("oldEmpId")
                .firstName("OldName")
                .districtId("oldDistId")
                .jobTitle("OldJob")
                .fleetId("oldFleetId")
                .secondName("OldDoe")
                .supervisor(false)
                .organizationId("org123")
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
        String id = "personnelId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("emp123")
                .supervisor(true)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .organizationId("org123")
                .build();

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(expectedDto), null));

        // Act
        PersonnelDto result = personnelService.findById(id);

        // Assert
        assertEquals(expectedDto, result);
    }

    @Test
    public void shouldDeletePersonnelWhenDeleteById() {
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
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Arrange
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist123";
        String jobTitle = "Developer";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.emptyList(), null));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(TotalCountObject.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(new TotalCountObject(0L)), null));

        // Act
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, result);
    }
}
