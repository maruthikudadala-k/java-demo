
package com.carbo.fleet.services;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.model.TotalCountObject;
import com.carbo.fleet.repository.PersonnelDBRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.domain.Pageable;
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

        when(personnelDBRepository.findByOrganizationId(organizationId, Pageable.ofSize(limit).withPage(offSet))).thenReturn(Optional.empty());
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDisplay.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        // Act
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelDBRepository).findByOrganizationId(organizationId, Pageable.ofSize(limit).withPage(offSet));
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Manager")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.save(any())).thenReturn(new Personnel());

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertTrue(result);
        verify(personnelDBRepository).save(any());
    }

    @Test
    public void shouldReturnFalseWhenDuplicateKeyExceptionOccursWhileSavingPersonnel() {
        // Arrange
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew123")
                .employeeId("emp123")
                .firstName("John")
                .districtId("dist123")
                .jobTitle("Manager")
                .fleetId("fleet123")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.save(any())).thenThrow(new DuplicateKeyException("Duplicate key"));

        // Act
        Boolean result = personnelService.savePersonnel(dto);

        // Assert
        assertFalse(result);
        verify(personnelDBRepository).save(any());
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Arrange
        String id = "personnel123";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Manager")
                .employeeId("emp123")
                .districtId("dist123")
                .supervisor(true)
                .organizationId("org123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(new Personnel()));

        // Act
        PersonnelDto actualDto = personnelService.findById(id);

        // Assert
        assertEquals(expectedDto, actualDto);
        verify(personnelDBRepository).findById(id);
    }

    @Test
    public void shouldDeletePersonnelWhenIdExists() {
        // Arrange
        String id = "personnel123";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(new Personnel()));

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldNotDeletePersonnelWhenIdDoesNotExist() {
        // Arrange
        String id = "personnel123";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        personnelService.deletePersonnel(id);

        // Assert
        verify(personnelDBRepository, never()).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Arrange
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist123";
        String jobTitle = "Manager";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDisplay.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        // Act
        PersonnelDisplay actualDisplay = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Assert
        assertEquals(expectedDisplay, actualDisplay);
    }
}
