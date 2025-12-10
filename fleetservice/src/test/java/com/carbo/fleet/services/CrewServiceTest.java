
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
        String id = "123";
        CrewDto crewDto = CrewDto.builder().id(id).name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").fleetId("FLEET123").build();
        List<CrewDto> crewList = new ArrayList<>();
        crewList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewList).build();

        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(crewService).lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAll() {
        String organizationId = "org123";
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).build();
        when(crewDbRepository.count()).thenReturn(0L);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        verify(crewDbRepository).count();
        verify(crewService).lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt());
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindingAllByFleet() {
        String organizationId = "org123";
        String fleetName = "FleetName";
        Pageable pageable = Pageable.ofSize(10);
        Fleet fleet = new Fleet();
        fleet.setId("FLEET123");
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);

        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(Collections.emptyList()).build();
        when(crewService.lookUpCrew(isNull(), eq(fleet.getId()), eq(organizationId), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
        verify(crewService).lookUpCrew(isNull(), eq(fleet.getId()), eq(organizationId), anyInt(), anyInt());
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsValid() {
        CrewDto crewDto = CrewDto.builder().id("123").name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").organizationId("org123").fleetId("FLEET123").build();
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
    public void shouldReturnTrueWhenUpdatingCrewIfPresent() {
        CrewDto crewDto = CrewDto.builder().id("123").name("John Doe").jobPattern("Pattern").shiftStart("08:00").startDate("01/01/2023").organizationId("org123").fleetId("FLEET123").build();
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
    public void shouldDeleteCrewWhenPresent() {
        String id = "123";
        Crew crew = new Crew();
        crew.setId(id);
        when(crewDbRepository.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(id);

        verify(crewDbRepository).findById(id);
        verify(crewDbRepository).deleteById(id);
    }
}
