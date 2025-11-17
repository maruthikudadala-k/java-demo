
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
import org.springframework.data.domain.Pageable;

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
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelDBRepository.findByOrganizationId(anyString(), any(Pageable.class))).thenReturn(Optional.empty());
        
        PersonnelDisplay result = personnelService.findAll("orgId", 0, 10);

        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
        assertTrue(result.getPersonnelDisplayObject().isEmpty());
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("John")
                .districtId("districtId")
                .jobTitle("Developer")
                .fleetId("fleetId")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("orgId")
                .build();

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(Personnel.builder().id("newId").build());

        Boolean result = personnelService.savePersonnel(dto);

        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnFalseWhenSavePersonnelThrowsDuplicateKeyException() {
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("John")
                .districtId("districtId")
                .jobTitle("Developer")
                .fleetId("fleetId")
                .secondName("Doe")
                .supervisor(true)
                .organizationId("orgId")
                .build();

        when(personnelDBRepository.save(any(Personnel.class))).thenThrow(new DuplicateKeyException("duplicate key"));

        Boolean result = personnelService.savePersonnel(dto);

        assertFalse(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        PersonnelDto dto = PersonnelDto.builder()
                .id("existingId")
                .crewId("crewId")
                .employeeId("employeeId")
                .firstName("John")
                .districtId("districtId")
                .jobTitle("Developer")
                .fleetId("fleetId")
                .secondName("Doe")
                .supervisor(true)
                .supervisorId("supervisorId")
                .organizationId("orgId")
                .build();

        when(personnelDBRepository.findById("existingId")).thenReturn(Optional.of(new Personnel()));
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(new Personnel());

        Boolean result = personnelService.updatePersonnel(dto);

        assertTrue(result);
        verify(personnelDBRepository, times(1)).findById("existingId");
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id("existingId")
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("employeeId")
                .supervisor(true)
                .organizationId("orgId")
                .build();

        when(personnelDBRepository.findById("existingId")).thenReturn(Optional.of(new Personnel()));

        PersonnelDto result = personnelService.findById("existingId");

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenDeletePersonnelCalled() {
        when(personnelDBRepository.findById("existingId")).thenReturn(Optional.of(new Personnel()));

        personnelService.deletePersonnel("existingId");

        verify(personnelDBRepository, times(1)).deleteById("existingId");
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelDBRepository.findByOrganizationId(anyString(), any(Pageable.class))).thenReturn(Optional.empty());

        PersonnelDisplay result = personnelService.findbyValue("orgId", "John", "districtId", "Developer", 0, 10);

        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
        assertTrue(result.getPersonnelDisplayObject().isEmpty());
    }
}
