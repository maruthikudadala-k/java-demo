
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
import org.mockito.junit.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public void shouldReturnCrewDtoWhenFindByIdIsCalled() {
        String id = "1";
        CrewDto crewDto = CrewDto.builder().id(id).name("John Doe").jobPattern("Job1").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").organizationId("Org1").build();
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(new ArrayList<>()).build();
        crewDisplayObject.getCrews().add(crewDto);

        when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(id);

        assertNotNull(result);
        assertEquals(crewDto.getId(), result.getId());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        String organizationId = "Org1";
        int offSet = 0;
        int limit = 10;

        when(crewDbRepository.count()).thenReturn(5L);
        when(crewService.lookUpCrew(null, null, organizationId, offSet, limit)).thenReturn(new CrewDisplayObject());

        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        assertNotNull(result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        String organizationId = "Org1";
        String fleetName = "Fleet1";
        int offSet = 0;
        int limit = 10;

        Pageable pageable = PageRequest.of(offSet / limit, limit);
        Fleet fleet = new Fleet();
        fleet.setId("Fleet1");

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(null, fleet.getId(), organizationId, offSet, limit)).thenReturn(new CrewDisplayObject());

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        assertNotNull(result);
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Job1").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").organizationId("Org1").build();
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
    public void shouldReturnTrueWhenUpdateCrewIsCalledAndCrewExists() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Job1").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").organizationId("Org1").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(crew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalledAndCrewExists() {
        String id = "1";
        Crew crew = new Crew();
        crew.setId(id);

        when(crewDbRepository.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(id);

        verify(crewDbRepository).deleteById(id);
    }
}
