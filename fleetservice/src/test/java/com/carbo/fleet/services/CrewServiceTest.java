
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public void shouldReturnCrewDtoWhenIdExists() {
        // Given
        String crewId = "crewId";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern 1")
                .shiftStart("08:00").startDate("01/01/2022").fleetId("fleetId").build();
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).totalCount(1).build();

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // When
        CrewDto result = crewService.findById(crewId);

        // Then
        assertEquals(crewDto, result);
    }

    @Test
    public void shouldReturnNullWhenIdDoesNotExist() {
        // Given
        String crewId = "nonExistingId";

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        CrewDto result = crewService.findById(crewId);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        // Given
        String organizationId = "orgId";
        int offset = 0;
        int limit = 10;
        Long totalCount = 5L;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(totalCount).build();
        Mockito.when(crewDbRepository.count()).thenReturn(totalCount);
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // When
        CrewDisplayObject result = crewService.findAll(organizationId, offset, limit);

        // Then
        assertEquals(totalCount, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        // Given
        String organizationId = "orgId";
        String fleetName = "Fleet 1";
        int offset = 0;
        int limit = 10;
        String fleetId = "fleetId";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(1).build();

        Mockito.when(mongoTemplate.findOne(any(), any())).thenReturn(new Fleet());
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        // When
        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offset, limit);

        // Then
        assertEquals(1, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenValidCrewDtoIsProvided() {
        // Given
        CrewDto crewDto = CrewDto.builder().id("crewId").name("John Doe").jobPattern("Pattern 1")
                .shiftStart("08:00").startDate("01/01/2022").organizationId("orgId").fleetId("fleetId").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        // When
        Crew result = crewService.saveCrew(crewDto);

        // Then
        assertEquals(crew.getId(), result.getId());
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewIsSuccessful() {
        // Given
        CrewDto crewDto = CrewDto.builder().id("crewId").name("John Doe").jobPattern("Pattern 1")
                .shiftStart("08:00").startDate("01/01/2022").organizationId("orgId").fleetId("fleetId").build();
        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(new Crew());

        // When
        Boolean result = crewService.updateCrew(crewDto);

        // Then
        assertEquals(true, result);
    }

    @Test
    public void shouldDeleteCrewWhenIdExists() {
        // Given
        String crewId = "crewId";
        Crew crew = new Crew();
        crew.setId(crewId);
        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(crew));

        // When
        crewService.deleteCrew(crewId);

        // Then
        Mockito.verify(crewDbRepository, Mockito.times(1)).deleteById(crewId);
    }
}
