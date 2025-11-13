
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
    public void shouldReturnPersonnelDisplayWhenFindAllIsCalled() {
        // Given
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        
        when(personnelService.lookUpPersonnel(null, "", "", "", organizationId, offSet, limit)).thenReturn(expectedDisplay);

        // When
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Then
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).lookUpPersonnel(null, "", "", "", organizationId, offSet, limit);
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnelIsCalled() {
        // Given
        PersonnelDto personnelDto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Driver")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.save(any())).thenReturn(new Personnel());

        // When
        Boolean result = personnelService.savePersonnel(personnelDto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository).save(any());
    }

    @Test
    public void shouldReturnFalseWhenSavePersonnelThrowsDuplicateKeyException() {
        // Given
        PersonnelDto personnelDto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Driver")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("org123")
                .build();

        when(personnelDBRepository.save(any())).thenThrow(new DuplicateKeyException("Duplicate key"));

        // When
        Boolean result = personnelService.savePersonnel(personnelDto);

        // Then
        assertFalse(result);
        verify(personnelDBRepository).save(any());
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnelIsCalled() {
        // Given
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id("1")
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Driver")
                .fleetId("fleet1")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("org123")
                .build();

        when(personnelDBRepository.findById("1")).thenReturn(Optional.of(new Personnel()));
        when(personnelDBRepository.save(any())).thenReturn(new Personnel());

        // When
        Boolean result = personnelService.updatePersonnel(personnelDto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository).findById("1");
        verify(personnelDBRepository).save(any());
    }

    @Test
    public void shouldReturnNullWhenFindByIdIsCalledWithNonExistingId() {
        // Given
        String id = "non-existing-id";
        when(personnelService.lookUpPersonnel(Collections.singletonList(id), "", "", "", null, 0, 10))
                .thenReturn(PersonnelDisplay.builder().personnelDisplayObject(Collections.emptyList()).build());

        // When
        PersonnelDto result = personnelService.findById(id);

        // Then
        assertNull(result);
        verify(personnelService).lookUpPersonnel(Collections.singletonList(id), "", "", "", null, 0, 10);
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelIsCalledWithExistingId() {
        // Given
        String id = "existing-id";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(new Personnel()));

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValueIsCalled() {
        // Given
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist1";
        String jobTitle = "Driver";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelService.lookUpPersonnel(null, personnelName, districtId, jobTitle, organizationId, offSet, limit)).thenReturn(expectedDisplay);

        // When
        PersonnelDisplay actualDisplay = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Then
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelService).lookUpPersonnel(null, personnelName, districtId, jobTitle, organizationId, offSet, limit);
    }
}
