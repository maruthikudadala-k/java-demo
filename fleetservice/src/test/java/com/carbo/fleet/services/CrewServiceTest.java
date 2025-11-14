
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
        CrewDto crewDto = CrewDto.builder().id("1").name("John").jobPattern("Full-time").startDate("01/01/2023").shiftStart("Morning").fleetId("fleet1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(crewDto)).build();

        Mockito.when(crewDbRepository.findById(eq("1"))).thenReturn(Optional.of(new Crew()));
        Mockito.when(crewService.lookUpCrew(any(), any(), any(), any(), any())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById("1");

        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();

        Mockito.when(crewDbRepository.count()).thenReturn(0L);
        Mockito.when(crewService.lookUpCrew(any(), any(), eq("org1"), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll("org1", 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");

        Mockito.when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        Mockito.when(crewService.lookUpCrew(any(), eq("fleet1"), eq("org1"), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet("org1", "fleetName", 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenSaveCrew() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John").jobPattern("Full-time").startDate("01/01/2023").shiftStart("Morning").fleetId("fleet1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId("org1");
        crew.setFleetId(crewDto.getFleetId());

        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrew() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John").jobPattern("Full-time").startDate("01/01/2023").shiftStart("Morning").fleetId("fleet1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());

        Mockito.when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));
        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        Crew crew = new Crew();
        crew.setId("1");

        Mockito.when(crewDbRepository.findById("1")).thenReturn(Optional.of(crew));

        crewService.deleteCrew("1");

        Mockito.verify(crewDbRepository).deleteById("1");
    }
}
