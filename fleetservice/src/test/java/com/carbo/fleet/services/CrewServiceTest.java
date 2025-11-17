
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
import java.util.regex.Pattern;

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
    public void shouldReturnCrewDtoWhenValidIdIsProvided() {
        String id = "crewId";
        CrewDto crewDto = CrewDto.builder().id(id).build();
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).build();

        when(crewDbRepository.findById(anyString())).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(id);

        assertEquals(crewDto, result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenValidParamsAreProvided() {
        String organizationId = "orgId";
        int offSet = 0;
        int limit = 10;
        Long totalCount = 10L;
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().totalCount(totalCount).build();

        when(crewDbRepository.count()).thenReturn(totalCount);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), eq(offSet), eq(limit))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAll(organizationId, offSet, limit);

        assertEquals(crewDisplayObject, result);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFleetNameIsProvided() {
        String organizationId = "orgId";
        String fleetName = "fleetName";
        int offSet = 0;
        int limit = 10;
        String fleetId = "fleetId";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().build();

        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(isNull(), eq(fleetId), eq(organizationId), eq(offSet), eq(limit))).thenReturn(crewDisplayObject);

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, offSet, limit);

        assertEquals(crewDisplayObject, result);
    }

    @Test
    public void shouldSaveCrewWhenCrewDtoIsProvided() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("Crew Name")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .jobPattern("jobPattern")
                .shiftStart("shiftStart")
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

        assertEquals(crew, result);
    }

    @Test
    public void shouldUpdateCrewWhenCrewDtoIsProvided() {
        CrewDto crewDto = CrewDto.builder()
                .id("crewId")
                .name("Updated Crew Name")
                .startDate("01/01/2023")
                .organizationId("orgId")
                .fleetId("fleetId")
                .jobPattern("jobPattern")
                .shiftStart("shiftStart")
                .build();
        Crew existingCrew = new Crew();
        existingCrew.setId(crewDto.getId());

        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(existingCrew));
        when(crewDbRepository.save(any(Crew.class))).thenReturn(existingCrew);

        Boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
    }

    @Test
    public void shouldDeleteCrewWhenIdIsProvided() {
        String id = "crewId";
        Crew crew = new Crew();
        crew.setId(id);

        when(crewDbRepository.findById(id)).thenReturn(Optional.of(crew));

        crewService.deleteCrew(id);

        verify(crewDbRepository, times(1)).deleteById(id);
    }
}
