
package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.model.Fleet;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class CrewServiceTest {

    @Mock
    private CrewDbRepository crewDbRepository;

    @Mock
    private FleetMongoDbRepository fleetMongoDbRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CrewService crewService;

    @Test
    void shouldReturnCrewDtoWhenFindByIdIsCalled() {
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(CrewDto.builder()
                        .id("1")
                        .name("Test Crew")
                        .jobPattern("Job Pattern")
                        .shiftStart("08:00")
                        .startDate("01/01/2021")
                        .organizationId("Org1")
                        .fleetId("Fleet1")
                        .build()))
                .build();

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById("1");
        assertNotNull(result);
        assertEquals("Test Crew", result.getName());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        Mockito.when(crewDbRepository.count()).thenReturn(1L);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(1)
                .build();

        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll("Org1", 0, 10);
        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        Mockito.when(crewDbRepository.count()).thenReturn(1L);
        Fleet fleet = new Fleet();
        fleet.setId("Fleet1");
        Mockito.when(mongoTemplate.findOne(any(), any())).thenReturn(fleet);

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(1)
                .build();

        Mockito.when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet("Org1", "FleetName", 0, 10);
        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void shouldSaveCrewWhenSaveCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("Test Crew")
                .jobPattern("Job Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2021")
                .organizationId("Org1")
                .fleetId("Fleet1")
                .build();

        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        Mockito.when(crewDbRepository.save(any())).thenReturn(crew);
        
        Crew result = crewService.saveCrew(crewDto);
        assertNotNull(result);
        assertEquals("Test Crew", result.getName());
    }

    @Test
    void shouldUpdateCrewWhenUpdateCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("Updated Crew")
                .jobPattern("Job Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2021")
                .organizationId("Org1")
                .fleetId("Fleet1")
                .build();

        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(existingCrew));
        Mockito.when(crewDbRepository.save(any())).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);
        assertTrue(result);
    }

    @Test
    void shouldDeleteCrewWhenDeleteCrewIsCalled() {
        Crew crew = new Crew();
        crew.setId("1");

        Mockito.when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(crew));
        Mockito.doNothing().when(crewDbRepository).deleteById(anyString());

        crewService.deleteCrew("1");
        Mockito.verify(crewDbRepository).deleteById("1");
    }
}
