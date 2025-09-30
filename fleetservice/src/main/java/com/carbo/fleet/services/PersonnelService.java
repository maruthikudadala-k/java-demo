package com.carbo.fleet.services;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.Personnel;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.model.TotalCountObject;
import com.carbo.fleet.repository.PersonnelDBRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {

    public static final Logger logger = LoggerFactory.getLogger(CrewService.class);

    MongoTemplate mongoTemplate;
    PersonnelDBRepository personnelDBRepository;

    @Autowired
    public PersonnelService(MongoTemplate mongoTemplate, PersonnelDBRepository personnelDBRepository) {
        this.mongoTemplate = mongoTemplate;
        this.personnelDBRepository = personnelDBRepository;
    }

    public PersonnelDisplay findAll(String organizationId, int offSet, int limit) {
        return lookUpPersonnel(null, "", "", "", organizationId, offSet, limit);
    }

    public Boolean savePersonnel(PersonnelDto dto) {
        try {
            Personnel newPersonnel = Personnel.builder()
                    .crewId(dto.getCrewId())
                    .employeeId(dto.getEmployeeId())
                    .firstName(dto.getFirstName())
                    .districtId(dto.getDistrictId())
                    .jobTitle(dto.getJobTitle())
                    .fleetId(dto.getFleetId())
                    .secondName(dto.getSecondName())
                    .supervisor(dto.getSupervisor())
                    .organizationId(dto.getOrganizationId())
                    .build();
            if (dto.getSupervisorId() != null)
                newPersonnel.setSupervisorId(dto.getSupervisorId());
            Personnel createdPersonnel = personnelDBRepository.save(newPersonnel);
            if (dto.getSupervisor() && dto.getSupervisorId() == null) {
                newPersonnel.setSupervisorId(createdPersonnel.getId());
                personnelDBRepository.save(newPersonnel);
            }
            return true;
        } catch (DuplicateKeyException e) {
            logger.error("duplicate key Value inside Personnel");
            return false;
        }
    }

    public Boolean updatePersonnel(PersonnelDto dto) {
        try {
            Optional<Personnel> personnelData = personnelDBRepository.findById(dto.getId());
            if (personnelData.isPresent()) {
                Personnel personnel = Personnel.builder()
                        .id(dto.getId())
                        .crewId(dto.getCrewId())
                        .employeeId(dto.getEmployeeId())
                        .firstName(dto.getFirstName())
                        .districtId(dto.getDistrictId())
                        .jobTitle(dto.getJobTitle())
                        .fleetId(dto.getFleetId())
                        .secondName(dto.getSecondName())
                        .supervisor(dto.getSupervisor())
                        .supervisorId(dto.getSupervisorId())
                        .organizationId(dto.getOrganizationId())
                        .build();
                personnelDBRepository.save(personnel);
                return true;
            }
        } catch (Exception e) {
            logger.error("Exception while updating personnel" + e.getMessage());
        }
        return false;
    }

    public PersonnelDto findById(String id) {
        ArrayList<String> list = new ArrayList<>();
        list.add(id);
        PersonnelDisplay personnel = lookUpPersonnel(list,"", "","",null, 0, 10);
        return personnel.getPersonnelDisplayObject().stream().findFirst().orElse(null);
    }

    public void deletePersonnel(String id) {
        Optional<Personnel> personnel = personnelDBRepository.findById(id);
        if (personnel.isPresent()) {
            personnelDBRepository.deleteById(id);
        }
    }

    public PersonnelDisplay lookUpPersonnel
            (List<String> personnelId, String personnelName, String districtId, String jobTitle, String organizationId, int offSet, int limit) {

        LookupOperation lookupFleet = LookupOperation.newLookup()
                .from("fleets")
                .localField("fleetId")
                .foreignField("_id")
                .as("fleet");

        UnwindOperation unwindFleet = Aggregation.unwind("fleet", true);

        LookupOperation lookupDistrict = LookupOperation.newLookup()
                .from("districts")
                .localField("districtId")
                .foreignField("_id")
                .as("district");

        UnwindOperation unwindDistrict = Aggregation.unwind("district", true);
        AggregationOperation addFields = context -> new Document("$addFields",
                new Document("crewIdObj",
                        new Document("$toObjectId", "$crewId")
                )
        );
        LookupOperation lookupCrew = LookupOperation.newLookup()
                .from("crews")
                .localField("crewIdObj")
                .foreignField("_id")
                .as("crew");

        UnwindOperation unwindCrew = Aggregation.unwind("crew", true);

        AggregationOperation addSupervisorIdObj = context -> new Document("$addFields",
                new Document("supervisorIdObj",
                        new Document("$toObjectId", "$supervisorId")
                )
        );

        LookupOperation lookupSupervisor = LookupOperation.newLookup()
                .from("personnel")             // same collection
                .localField("supervisorIdObj")   // field in current doc
                .foreignField("_id")          // match with `_id` of supervisor
                .as("supervisorInfo");

        UnwindOperation unwindsupervisorInfo = Aggregation.unwind("supervisorInfo", true);

        ProjectionOperation project = Aggregation.java_springboot-master()
                .and("fleet.name").as("fleetName")
                .and("district.name").as("districtName")
                .and("crew.name").as("crewName")
                .and("supervisorInfo.firstName" ) .as("supervisorName")
                .andInclude("firstName", "secondName", "jobTitle", "employeeId", "supervisorId", "supervisor", "organizationId","districtId","crewId","fleetId");

        SkipOperation skip = Aggregation.skip((long) offSet);
        LimitOperation limitValue = Aggregation.limit(limit);
        MatchOperation match;
        if (personnelId != null) {
            match = Aggregation.match(Criteria.where("_id").in(personnelId));
        } else if (StringUtils.hasText(personnelName) && StringUtils.hasText(districtId) && StringUtils.hasText(jobTitle)) {
            match = Aggregation.match(
                    new Criteria().andOperator(
                            Criteria.where("districtId").is(districtId),
                            Criteria.where("jobTitle").is(jobTitle),
                            new Criteria().orOperator(
                                    Criteria.where("firstName").is(personnelName),
                                    Criteria.where("secondName").is(personnelName)
                            )
                    )
            );

        } else if (StringUtils.hasText(personnelName) && StringUtils.hasText(districtId)) {
            match = Aggregation.match(
                    new Criteria().andOperator(
                            Criteria.where("districtId").is(districtId),
                            new Criteria().orOperator(
                                    Criteria.where("firstName").is(personnelName),
                                    Criteria.where("secondName").is(personnelName)
                            )
                    )
            );

        } else if (!personnelName.isEmpty()) {
            match = Aggregation.match(
                    new Criteria().orOperator(
                            Criteria.where("firstName").is(personnelName),
                            Criteria.where("secondName").is(personnelName)
                    )
            );

        } else {
            match = Aggregation.match(Criteria.where("organizationId").is(organizationId));
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match,
                lookupFleet,
                unwindFleet,
                lookupDistrict,
                unwindDistrict,
                addFields,
                lookupCrew,
                unwindCrew,
                addSupervisorIdObj,
                lookupSupervisor,
                unwindsupervisorInfo,
                project,
                skip,
                limitValue
        );

        Aggregation countAggregation = Aggregation.newAggregation(
                match,
                Aggregation.count().as("totalCount")
        );
        TotalCountObject totalCountObject = mongoTemplate.aggregate(countAggregation,"personnel",TotalCountObject.class).getUniqueMappedResult();
        return PersonnelDisplay.builder()
                .personnelDisplayObject(mongoTemplate.aggregate(aggregation, "personnel", PersonnelDto.class).getMappedResults())
                .totalCount(totalCountObject != null ? totalCountObject.getTotalCount() : 0)
                .build();
    }

    public PersonnelDisplay findbyValue(String organizationId, String personnelName, String districtId, String jobTitle, int offSet, int limit) {
        return lookUpPersonnel(null, personnelName, districtId, jobTitle, organizationId, offSet, limit);
    }
}
