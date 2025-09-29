
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
        CrewDto crewDto = CrewDto.builder().id(crewId).name("Crew Member").build();
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).build();

        Mockito.when(crewService.lookUpCrew(any(), any(), any(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "org1";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).build();

        Mockito.when(crewDbRepository.count()).thenReturn(10L);
        Mockito.when(crewService.lookUpCrew(any(), any(), eq(organizationId), eq(offSet), eq(limit))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        assertNotNull(result);
        assertEquals(0, result.getCrews().size());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "org1";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;
        Fleet fleet = new Fleet();
        fleet.setId("fleet1");
        List<CrewDto> crewDtoList = new ArrayList<>();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).build();

        Mockito.when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        Mockito.when(crewService.lookUpCrew(any(), eq(fleet.getId()), eq(organizationId), eq(offSet), eq(limit))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        assertNotNull(result);
        assertEquals(0, result.getCrews().size());
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewCalled() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("Crew Member")
                .startDate("01/01/2023")
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

        Mockito.when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew result = crewService.saveCrew(crewDto);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnTrueWhenUpdateCrewCalled() {
        CrewDto crewDto = CrewDto.builder()
                .id("1")
                .name("Updated Crew Member")
                .startDate("01/01/2023")
                .organizationId("org1")
                .fleetId("fleet1")
                .jobPattern("Job Pattern")
                .shiftStart("Shift Start")
                .build();

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

        Mockito.verify(crewDbRepository, Mockito.times(1)).deleteById(crewId);
    }
}
