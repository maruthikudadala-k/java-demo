
package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.TotalCountObject;
import com.carbo.fleet.repository.CrewDbRepository;
import com.carbo.fleet.repository.FleetMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class CrewServiceTest {

    @Mock
    private CrewDbRepository crewDbRepository;

    @Mock
    private FleetMongoDbRepository fleetMongoDbRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CrewService crewService;

    @Test
    public void shouldReturnCrewDtoWhenFindByIdIsCalledWithValidId() {
        // Arrange
        String crewId = "crewId";
        CrewDto crewDto = CrewDto.builder()
                .id(crewId)
                .name("John Doe")
                .jobPattern("Pattern A")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(crewDto))
                .totalCount(1)
                .build();

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // Act
        CrewDto result = crewService.findById(crewId);

        // Assert
        assertEquals(crewDto, result);
    }

    @Test
    public void shouldReturnNullWhenFindByIdIsCalledWithInvalidId() {
        // Arrange
        String crewId = "crewId";
        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(CrewDisplayObject.builder().crews(Collections.emptyList()).build());

        // Act
        CrewDto result = crewService.findById(crewId);

        // Assert
        assertNull(result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        // Arrange
        String organizationId = "orgId";
        int offset = 0;
        int limit = 10;
        Long totalCount = 5L;

        Mockito.when(crewDbRepository.count()).thenReturn(totalCount);
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(totalCount).build());

        // Act
        CrewDisplayObject result = crewService.findAll(organizationId, offset, limit);

        // Assert
        assertEquals(totalCount, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        // Arrange
        String organizationId = "orgId";
        String fleetName = "fleetName";
        int offset = 0;
        int limit = 10;

        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        Mockito.when(mongoTemplate.findOne(any(), Mockito.eq(Fleet.class))).thenReturn(fleet);
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(CrewDisplayObject.builder().crews(new ArrayList<>()).build());

        // Act
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offset, limit);

        // Assert
        assertEquals(0, result.getCrews().size());
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalledWithValidCrewDto() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .jobPattern("Pattern A")
                .shiftStart("08:00")
                .build();

        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        // Act
        Crew result = crewService.saveCrew(crewDto);

        // Assert
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnFalseWhenUpdateCrewIsCalledWithNonExistingCrew() {
        // Arrange
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("John Doe")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .jobPattern("Pattern A")
                .shiftStart("08:00")
                .build();

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Boolean result = crewService.updateCrew(crewDto);

        // Assert
        assertEquals(false, result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalledWithExistingCrew() {
        // Arrange
        String crewId = "crewId";
        Crew crew = new Crew();
        crew.setId(crewId);
        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(crew));

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        Mockito.verify(crewDbRepository, Mockito.times(1)).deleteById(crewId);
    }

    @Test
    public void shouldNotDeleteCrewWhenDeleteCrewIsCalledWithNonExistingCrew() {
        // Arrange
        String crewId = "crewId";
        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        crewService.deleteCrew(crewId);

        // Assert
        Mockito.verify(crewDbRepository, Mockito.never()).deleteById(anyString());
    }
}
