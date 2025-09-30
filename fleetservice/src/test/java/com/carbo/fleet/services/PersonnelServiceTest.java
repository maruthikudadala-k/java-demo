
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
import org.springframework.data.domain.PageRequest;

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
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        when(personnelDBRepository.findByOrganizationId(organizationId, PageRequest.of(offSet / limit, limit)))
                .thenReturn(Optional.empty());
        when(mongoTemplate.aggregate(any(), eq("personnel"), eq(PersonnelDto.class)))
                .thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        // When
        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, offSet, limit);

        // Then
        assertEquals(expectedDisplay, actualDisplay);
        verify(personnelDBRepository).findByOrganizationId(organizationId, PageRequest.of(offSet / limit, limit));
    }

    @Test
    public void shouldSavePersonnelSuccessfully() {
        // Given
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew1")
                .employeeId("emp1")
                .firstName("John")
                .districtId("dist1")
                .jobTitle("Engineer")
                .fleetId("fleet1")
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

        when(personnelDBRepository.save(any(Personnel.class))).thenReturn(newPersonnel);

        // When
        Boolean result = personnelService.savePersonnel(dto);

        // Then
        assertTrue(result);
        verify(personnelDBRepository).save(newPersonnel);
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindById() {
        // Given
        String id = "personnelId";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Engineer")
                .employeeId("emp1")
                .supervisor(false)
                .districtId("dist1")
                .fleetId("fleet1")
                .crewId("crew1")
                .organizationId("org123")
                .build();

        PersonnelDisplay display = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();

        when(personnelService.lookUpPersonnel(anyList(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(display);

        // When
        PersonnelDto actualDto = personnelService.findById(id);

        // Then
        assertEquals(expectedDto, actualDto);
        verify(personnelService).lookUpPersonnel(anyList(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    public void shouldDeletePersonnelWhenExists() {
        // Given
        String id = "personnelId";
        Personnel personnel = Personnel.builder().id(id).build();

        when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldNotDeletePersonnelWhenNotExists() {
        // Given
        String id = "personnelId";

        when(personnelDBRepository.findById(id)).thenReturn(Optional.empty());

        // When
        personnelService.deletePersonnel(id);

        // Then
        verify(personnelDBRepository, never()).deleteById(id);
    }
}
