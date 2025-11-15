
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(List.of(crewDto)).build();

        when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(any(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewDbRepository).findById(crewId);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "org1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(any(), anyString(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(crewDbRepository).count();
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "org1";
        String fleetName = "FleetA";
        String fleetId = "fleet1";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).totalCount(0).build();
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(any(), eq(fleetId), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    public void shouldSaveCrewWhenSaveCrew() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("John Doe")
                .startDate("01/01/2021")
                .organizationId("org1")
                .fleetId("fleet1")
                .jobPattern("Job Pattern")
                .shiftStart("Shift Start")
                .build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrew() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("John Doe")
                .startDate("01/01/2021")
                .organizationId("org1")
                .fleetId("fleet1")
                .jobPattern("Job Pattern")
                .shiftStart("Shift Start")
                .build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
        verify(crewDbRepository).findById(crewDto.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrew() {
        String crewId = "1";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository).deleteById(crewId);
    }
}
