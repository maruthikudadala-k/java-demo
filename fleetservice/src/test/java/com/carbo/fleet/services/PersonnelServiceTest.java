
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String organizationId = "org123";
        PersonnelDisplay expectedDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.emptyList())
                .totalCount(0L)
                .build();

        Mockito.when(mongoTemplate.aggregate(Mockito.any(), Mockito.eq("personnel"), Mockito.eq(PersonnelDto.class)))
                .thenReturn(Mockito.mock(AggregationResults.class));
        Mockito.when(mongoTemplate.aggregate(Mockito.any(), Mockito.eq("personnel"), Mockito.eq(TotalCountObject.class)))
                .thenReturn(Mockito.mock(AggregationResults.class));
        
        Mockito.when(mongoTemplate.aggregate(Mockito.any(), Mockito.eq("personnel"), Mockito.eq(TotalCountObject.class)))
                .thenReturn(new AggregationResults<>(Collections.emptyList(), new TotalCountObject(0L)));

        PersonnelDisplay actualDisplay = personnelService.findAll(organizationId, 0, 10);

        assertEquals(expectedDisplay, actualDisplay);
    }

    @Test
    public void shouldReturnTrueWhenSavePersonnelIsCalled() {
        PersonnelDto dto = PersonnelDto.builder()
                .crewId("crew154")
                .employeeId("emp145")
                .firstName("John")
                .districtId("dist233")
                .jobTitle("Developer")
                .fleetId("fleet519")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org123")
                .build();

        Personnel personnel = Personnel.builder()
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

        Mockito.when(personnelDBRepository.save(Mockito.any(Personnel.class))).thenReturn(personnel);

        Boolean result = personnelService.savePersonnel(dto);

        assertTrue(result);
        Mockito.verify(personnelDBRepository).save(Mockito.any(Personnel.class));
    }

    @Test
    public void shouldReturnPersonnelDtoWhenFindByIdIsCalled() {
        String id = "person123";
        PersonnelDto expectedDto = PersonnelDto.builder()
                .id(id)
                .firstName("John")
                .secondName("Doe")
                .jobTitle("Developer")
                .employeeId("emp145")
                .supervisor(false)
                .districtId("dist233")
                .fleetId("fleet519")
                .organizationId("org123")
                .crewId("crew154")
                .build();

        PersonnelDisplay personnelDisplay = PersonnelDisplay.builder()
                .personnelDisplayObject(Collections.singletonList(expectedDto))
                .totalCount(1L)
                .build();

        Mockito.when(mongoTemplate.aggregate(Mockito.any(), Mockito.eq("personnel"), Mockito.eq(PersonnelDto.class)))
                .thenReturn(Mockito.mock(AggregationResults.class));
        
        Mockito.when(mongoTemplate.aggregate(Mockito.any(), Mockito.eq("personnel"), Mockito.eq(TotalCountObject.class)))
                .thenReturn(new AggregationResults<>(Collections.singletonList(expectedDto), new TotalCountObject(1L)));

        PersonnelDto actualDto = personnelService.findById(id);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void shouldDeletePersonnelWhenIdIsPassed() {
        String id = "person123";
        Personnel personnel = Personnel.builder().id(id).build();

        Mockito.when(personnelDBRepository.findById(id)).thenReturn(Optional.of(personnel));

        personnelService.deletePersonnel(id);

        Mockito.verify(personnelDBRepository).deleteById(id);
    }

    @Test
    public void shouldReturnTrueWhenUpdatePersonnelIsCalled() {
        PersonnelDto dto = PersonnelDto.builder()
                .id("person123")
                .crewId("crew154")
                .employeeId("emp145")
                .firstName("John")
                .districtId("dist233")
                .jobTitle("Developer")
                .fleetId("fleet519")
                .secondName("Doe")
                .supervisor(false)
                .organizationId("org123")
                .build();

        Personnel existingPersonnel = Personnel.builder()
                .id(dto.getId())
                .crewId("crewPrevious")
                .employeeId("empPrevious")
                .firstName("Old")
                .districtId("distPrevious")
                .jobTitle("Old Job")
                .fleetId("fleetPrevious")
                .secondName("OldName")
                .supervisor(false)
                .organizationId("org123")
                .build();

        Mockito.when(personnelDBRepository.findById(dto.getId())).thenReturn(Optional.of(existingPersonnel));

        Boolean result = personnelService.updatePersonnel(dto);

        assertTrue(result);
        Mockito.verify(personnelDBRepository).save(Mockito.any(Personnel.class));
    }
}
