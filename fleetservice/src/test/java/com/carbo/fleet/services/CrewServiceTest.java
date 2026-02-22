
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
        String id = "crewId";
        CrewDto crewDto = CrewDto.builder().id(id).name("John Doe").jobPattern("Pattern").shiftStart("09:00").startDate("01/01/2023").fleetId("fleetId").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.singletonList(crewDto)).build();
        
        when(crewDbRepository.count()).thenReturn(1L);
        when(mongoTemplate.aggregate(any(), eq("crews"), eq(CrewDto.class))).thenReturn(new AggregationResults<>(Collections.singletonList(crewDto), null));

        CrewDto result = crewService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        String organizationId = "orgId";
        int offset = 0;
        int limit = 10;

        when(crewDbRepository.count()).thenReturn(1L);
        when(mongoTemplate.aggregate(any(), eq("crews"), eq(CrewDto.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), null));

        CrewDisplayObject result = crewService.findAll(organizationId, offset, limit);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        String organizationId = "orgId";
        String fleetName = "fleetName";
        int offset = 0;
        int limit = 10;
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewDbRepository.count()).thenReturn(1L);
        when(mongoTemplate.aggregate(any(), eq("crews"), eq(CrewDto.class))).thenReturn(new AggregationResults<>(Collections.emptyList(), null));

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offset, limit);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
    }

    @Test
    void shouldSaveCrewWhenSaveCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder().id("crewId").name("John Doe").jobPattern("Pattern").shiftStart("09:00").startDate("01/01/2023").organizationId("orgId").fleetId("fleetId").build();
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
    void shouldReturnTrueWhenUpdateCrewIsCalledAndCrewExists() {
        CrewDto crewDto = CrewDto.builder().id("crewId").name("John Doe").jobPattern("Pattern").shiftStart("09:00").startDate("01/01/2023").organizationId("orgId").fleetId("fleetId").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    void shouldDeleteCrewWhenDeleteCrewIsCalledAndCrewExists() {
        String id = "crewId";
        Crew crew = new Crew();
        crew.setId(id);

        when(crewDbRepository.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(id);

        verify(crewDbRepository).deleteById(id);
    }
}
