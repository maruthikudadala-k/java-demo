
package com.carbo.fleet.services;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.model.Fleet;
import com.carbo.fleet.model.TotalCountObject;
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
        String crewId = "123";
        CrewDto crewDto = CrewDto.builder().id(crewId).name("John Doe").jobPattern("Pattern A")
                .shiftStart("08:00").startDate("01/01/2022").fleetId("F1").organizationId("Org1").build();
        List<CrewDto> crewDtoList = new ArrayList<>();
        crewDtoList.add(crewDto);
        CrewDisplayObject crewDisplayObject = CrewDisplayObject.builder().crews(crewDtoList).totalCount(1).build();

        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));
        when(crewService.lookUpCrew(anyList(), isNull(), isNull(), anyInt(), anyInt())).thenReturn(crewDisplayObject);

        CrewDto result = crewService.findById(crewId);

        assertNotNull(result);
        assertEquals(crewId, result.getId());
        verify(crewDbRepository).findById(crewId);
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllIsCalled() {
        String organizationId = "Org1";
        Long totalCount = 5L;
        when(crewDbRepository.count()).thenReturn(totalCount);
        when(crewService.lookUpCrew(isNull(), isNull(), eq(organizationId), anyInt(), anyInt()))
                .thenReturn(new CrewDisplayObject(new ArrayList<>(), totalCount));

        CrewDisplayObject result = crewService.findAll(organizationId, 0, 10);

        assertNotNull(result);
        assertEquals(totalCount, result.getTotalCount());
        verify(crewDbRepository).count();
    }

    @Test
    public void shouldReturnCrewDisplayObjectWhenFindAllByFleetIsCalled() {
        String organizationId = "Org1";
        String fleetName = "Fleet1";
        String fleetId = "F1";
        Fleet fleet = new Fleet();
        fleet.setId(fleetId);
        when(mongoTemplate.findOne(any(), eq(Fleet.class))).thenReturn(fleet);
        when(crewService.lookUpCrew(isNull(), eq(fleetId), eq(organizationId), anyInt(), anyInt()))
                .thenReturn(new CrewDisplayObject(new ArrayList<>(), 0));

        CrewDisplayObject result = crewService.findAllByFleet(organizationId, fleetName, 0, 10);

        assertNotNull(result);
        verify(mongoTemplate).findOne(any(), eq(Fleet.class));
    }

    @Test
    public void shouldSaveCrewWhenSaveCrewIsCalled() {
        CrewDto crewDto = CrewDto.builder().id("123").name("John Doe").jobPattern("Pattern A")
                .shiftStart("08:00").startDate("01/01/2022").fleetId("F1").organizationId("Org1").build();
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
    public void shouldReturnTrueWhenUpdateCrewIsCalledWithExistingCrew() {
        CrewDto crewDto = CrewDto.builder().id("123").name("John Doe").jobPattern("Pattern A")
                .shiftStart("08:00").startDate("01/01/2022").fleetId("F1").organizationId("Org1").build();
        when(crewDbRepository.findById(crewDto.getId())).thenReturn(Optional.of(new Crew()));

        boolean result = crewService.updateCrew(crewDto);

        assertTrue(result);
        verify(crewDbRepository).findById(crewDto.getId());
        verify(crewDbRepository).save(any(Crew.class));
    }

    @Test
    public void shouldDeleteCrewWhenDeleteCrewIsCalledWithExistingCrew() {
        String crewId = "123";
        when(crewDbRepository.findById(crewId)).thenReturn(Optional.of(new Crew()));

        crewService.deleteCrew(crewId);

        verify(crewDbRepository).deleteById(crewId);
    }
}
