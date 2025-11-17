
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
    public void shouldReturnCrewDtoWhenFindById() {
        String id = "crewId";
        CrewDto crewDto = CrewDto.builder().id(id).name("Crew Name").jobPattern("Job Pattern")
                .shiftStart("08:00").startDate("01/01/2023").fleetId("fleetId").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(Collections.singletonList(crewDto))
                .totalCount(1)
                .build();

        Mockito.when(crewDbRepository.findById(any(), eq(id))).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "orgId";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        Mockito.when(crewDbRepository.count()).thenReturn(0L);
        Mockito.when(crewService.lookUpCrew(Mockito.isNull(), Mockito.isNull(), eq(organizationId), anyInt(), anyInt()))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "orgId";
        String fleetName = "Fleet Name";
        String fleetId = "fleetId";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        Mockito.when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        Mockito.when(crewService.lookUpCrew(Mockito.isNull(), eq(fleetId), eq(organizationId), anyInt(), anyInt()))
                .thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenSaveCrew() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("Crew Name")
                .jobPattern("Job Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
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

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrew() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("Crew Name")
                .jobPattern("Job Pattern")
                .shiftStart("08:00")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();
        
        Mockito.when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(new Crew());

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        String id = "crewId";
        Crew crew = new Crew();
        crew.setId(id);

        Mockito.when(crewDbRepository.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(id);

        Mockito.verify(crewDbRepository).deleteById(id);
    }
}
