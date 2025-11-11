
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
        String organizationId = "org123";
        int offSet = 0;
        int limit = 10;
        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();
        
        when(personnelDBRepository.findByOrganizationId(organizationId, Pageable.ofSize(limit).withPage(offSet))).thenReturn(Optional.of(Collections.emptyList()));
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), null));

        // When
        PersonnelDisplay result = personnelService.findAll(organizationId, offSet, limit);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Given
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
                .id("1")
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

        // When
        Boolean result = personnelService.savePersonnel(dto);

        // Then
        assertTrue(result);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Given
        String id = "1";
        PersonnelDto personnelDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp123")
                .supervisor(false)
                .districtId("dist123")
                .fleetId("fleet123")
                .crewId("crew123")
                .build();

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(personnelDto))
                .totalCount(1L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class))).thenReturn(new AggregationResults<>(Collections.singletonList(personnelDto), null));

        // When
        PersonnelDto result = personnelService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    public void shouldDeletePersonnelWhenIdExists() {
        // Given
        String id = "1";
        Personnel personnel = Personnel.builder().id(id).build();
        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldNotDeletePersonnelWhenIdDoesNotExist() {
        // Given
        String id = "1";
        when(personnelDBRepository.findById(id)).thenReturn(Optional.empty());

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository, never()).deleteById(any());
    }

    @Test
    public void shouldReturnPersonnelDisplayWhenFindByValue() {
        // Given
        String organizationId = "org123";
        String personnelName = "John";
        String districtId = "dist123";
        String jobTitle = "Engineer";
        int offSet = 0;
        int limit = 10;

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), null));

        // When
        PersonnelDisplay result = personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPersonnelDisplayObject().size());
    }
}
