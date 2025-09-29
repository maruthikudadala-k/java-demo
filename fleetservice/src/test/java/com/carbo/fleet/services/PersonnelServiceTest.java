
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
        // Given
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        // When
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Then
        assertNotNull(result);
        assertEquals(expectedDisplay.getTotalCount(), result.getTotalCount());
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnel() {
        // Given
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

        Personnel createdPersonnel = Personnel.builder().id("newId").build();
        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(createdPersonnel);
        
        // When
        Boolean result = personnelService.savePersonnel(dto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnel() {
        // Given
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
        
        Personnel existingPersonnel = Personnel.builder().id("existingId").build();
        when(personnelDBRepository.findById("existingId")).thenReturn(Optional.of(existingPersonnel));

        // When
        Boolean result = personnelService.updatePersonnel(dto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository, times(1)).save(any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Given
        String id = "existingId";
        PersonnelDto expectedDto = PersonnelDto.builder().id(id).firstName("John").secondName("Doe").build();
        PersonnelDisplay display = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();
        
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new AggregationResults<>(Collections.singletonList(expectedDto), new TotalCountObject(1L)));

        // When
        PersonnelDto result = personnelService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }

    @Test
    public void shouldDeletePersonnelWhenExists() {
        // Given
        String id = "existingId";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository, times(1)).deleteById(id);
    }
}
