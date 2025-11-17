
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
import org.mockito.junit.jupiter.MockitoExtension;
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
        String crewId = "1";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Driver")
                .shiftStart("08:00").startDate("01/01/2023").fleetId("fleet1").organizationId("org1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(crewDto)).build();

        Mockito.when(crewDbRepository.findById(eq("org1"), eq(crewId))).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(any(), any(), eq("org1"), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "org1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();

        Mockito.when(crewDbRepository.count()).thenReturn(0L);
        Mockito.when(crewService.lookUpCrew(null, null, eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "org1";
        String fleetName = "fleet1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");

        Mockito.when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        Mockito.when(crewService.lookUpCrew(null, fleet.getId(), organizationId, anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewCalled() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Driver")
                .shiftStart("08:00").startDate("01/01/2023").fleetId("fleet1").organizationId("org1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        
        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewCalled() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Driver")
                .shiftStart("08:00").startDate("01/01/2023").fleetId("fleet1").organizationId("org1").build();
        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        Mockito.when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewCalled() {
        String crewId = "1";
        Crew crew = new Crew();
        crew.setId(crewId);

        Mockito.when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(crewId);

        Mockito.verify(crewDbRepository).deleteById(crewId);
    }
}
