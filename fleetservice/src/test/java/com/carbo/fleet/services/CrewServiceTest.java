
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
        String crewId = "1";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").build();
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).build();

        when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(Collections.singletonList(crewId), null, null, 0, 10)).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewDbRepository).findById(crewId);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAll() {
        String organizationId = "Org1";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();

        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(null, null, organizationId, offSet, limit)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(crewDbRepository).count();
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleet() {
        String organizationId = "Org1";
        String fleetName = "Fleet1";
        int offSet = 0;
        int limit = 10;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).totalCount(0).build();
        Fleet fleet = new Fleet();
        fleet.setId("FleetId1");

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(null, fleet.getId(), organizationId, offSet, limit)).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        assertNotNull(result);
        assertEquals(0, result.getTotalCount());
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    public void shouldSaveCrewSuccessfully() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").organizationId("Org1").build();
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
    public void shouldReturnTrueWhenCrewUpdatedSuccessfully() {
        CrewDto crewDto = CrewDto.builder().id("1").name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("Fleet1").organizationId("Org1").build();
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
    public void shouldDeleteCrewWhenFound() {
        String crewId = "1";
        Crew crew = new Crew();
        crew.setId(crewId);

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository).deleteById(crewId);
    }
}
