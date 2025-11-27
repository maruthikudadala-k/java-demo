
package com.carbo.fleet.services;

import com.carbo.fleet.dto.PersonnelDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
        Mockito.when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.MongoTemplate.AggregationResults<>(Collections.emptyList(), expectedDisplay));

        // Act
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("district123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org123")
                .build();
        Mockito.when(personnelDBRepository.save(any())).thenReturn(new Personnel());

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        Mockito.verify(personnelDBRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void shouldUpdatePersonnelSuccessfully() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .id("personnel123")
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("district123")
                .jobTitle("Developer")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(false)
                .supervisorId("supervisor123")
                .organizationId("org123")
                .build();
        Mockito.when(personnelDBRepository.findById("personnel123")).thenReturn(Optional.of(new Personnel()));
        Mockito.when(personnelDBRepository.save(any())).thenReturn(new Personnel());

        // Act
        Boolean result = personnelService.updatePersonnel(dto);

        // Assert
        assertTrue(result);
        Mockito.verify(personnelDBRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "personnel123";
        PersonnelDto expectedDto = PersonnelDto.builder().id(id).firstName("John").build();
        PersonnelDisplay display = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();
        Mockito.when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.MongoTemplate.AggregationResults<>(Collections.singletonList(expectedDto), display));

        // Act
        PersonnelDto actualDto = personnelService.findById(id);

        // Assert
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void shouldDeletePersonnelSuccessfully() {
        // Arrange
        String id = "personnel123";
        Mockito.when(personnelDBRepository.findById(id)).thenReturn(Optional.of(new Personnel()));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        Mockito.verify(personnelDBRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Arrange
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "district123";
        String jobTitle = "Developer";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        Mockito.when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new org.springframework.data.mongodb.core.MongoTemplate.AggregationResults<>(Collections.emptyList(), expectedDisplay));

        // Act
        PersonnelDisplay actualDisplay = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
    }
}
