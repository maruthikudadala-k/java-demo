
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
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
    public void shouldReturnCrewDtoWhenIdExists() {
        String crewId = "123";
        CrewDto expectedCrewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Full-Time").shiftStart("09:00").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").build();
        List<CrewDto> crewDtos = new ArrayList<>();
        crewDtos.add(expectedCrewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtos).build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10)).thenReturn(crewDisplayObject);

        CrewDto actualCrewDto = crewService.findById(crewId);

        assertEquals(expectedCrewDto, actualCrewDto);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "org1";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(null, null, organizationId, offSet, limit)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewService.findAll(organizationId, offSet, limit);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "org1";
        String fleetName = "Fleet A";
        int offSet = 0;
        int limit = 10;
        Fleet fleet = new Fleet();
        fleet.setId("fleetId");
        CrewDisplayObject expectedCrewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(null, fleet.getId(), organizationId, offSet, limit)).thenReturn(expectedCrewDisplayObject);

        CrewDisplayObject actualCrewDisplayObject = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        assertEquals(expectedCrewDisplayObject, actualCrewDisplayObject);
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsValid() {
        CrewDto crewDto = CrewDto.builder().id("123").name("John Doe").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").jobPattern("Full-Time").shiftStart("09:00").build();
        Crew crew = new Crew();
        crew.setId(crewDto.getId());
        crew.setName(crewDto.getName());
        crew.setStartDate(LocalDate.parse(crewDto.getStartDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        crew.setOrganizationId(crewDto.getOrganizationId());
        crew.setFleetId(crewDto.getFleetId());
        crew.setJobPattern(crewDto.getJobPattern());
        crew.setShiftStart(crewDto.getShiftStart());

        when(crewDbRepository.save(any(Crew.class))).thenReturn(crew);

        Crew actualCrew = crewService.saveCrew(crewDto);

        assertNotNull(actualCrew);
        assertEquals(crewDto.getId(), actualCrew.getId());
    }

    @Test
    public void shouldReturnTrueWhenCrewIsUpdatedSuccessfully() {
        CrewDto crewDto = CrewDto.builder().id("123").name("Jane Doe").startDate("01/01/2023").organizationId("org1").fleetId("fleet1").jobPattern("Part-Time").shiftStart("10:00").build();
        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenIdExists() {
        String crewId = "123";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository).deleteById(crewId);
    }
}
