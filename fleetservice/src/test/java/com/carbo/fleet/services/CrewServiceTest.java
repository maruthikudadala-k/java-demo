
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
    public void shouldReturnCrewDtoWhenFoundById() {
        String crewId = "crewId";
        CrewDto crewDto = CrewDto.builder().id(crewId).build();
        List<CrewDto> crewDtoList = Collections.singletonList(crewDto);
        
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(crewDtoList)
                .totalCount(1)
                .build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);
        assertEquals(crewDto, result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "orgId";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "orgId";
        String fleetName = "fleetName";
        String fleetId = "fleetId";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder()
                .crews(new ArrayList<>())
                .totalCount(0)
                .build();

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(new Fleet());
        when(crewService.lookUpCrew(isNull(), eq(fleetId), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);
        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsValid() {
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

        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew result = crewService.saveCrew(crewDto);
        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldUpdateCrewWhenCrewDtoIsExisting() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("Updated Crew Name")
                .jobPattern("Updated Job Pattern")
                .shiftStart("09:00")
                .startDate("01/02/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .build();

        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);
        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenCrewExists() {
        String crewId = "crewId";
        Crew existingCrew = new Crew();
        existingCrew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(existingCrew));

        crewService.deleteCrew(crewId);
        verify(crewDbRepository, times(1)).deleteById(crewId);
    }
}
