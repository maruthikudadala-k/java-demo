
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

import java.util.ArrayList;
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
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(mongoTemplate, times(1)).aggregate(any(), eq("personnel"), eq(TotalCountObject.class));
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
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

        Personnel createdPersonnel = Personnel.builder()
                .id("newId")
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(createdPersonnel);

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
                .id("existingId")
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

        Personnel existingPersonnel = Personnel.builder()
                .id("existingId")
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

        when(personnelDBRepository.findById("existingId")).thenReturn(Optional.of(existingPersonnel));
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(existingPersonnel);

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository, times(1)).findById("existingId");
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
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(false)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .organizationId("org123")
                .build();

        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.AggregationResults<>(Collections.singletonList(expectedDto), null));

        // Act
        PersonnelDto actualDto = personnelService.findById(id);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(mongoTemplate, times(1)).aggregate(any(), eq("personnel"), eq(PersonnelDto.class));
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
}
