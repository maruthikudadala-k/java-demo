// ===== Imported from: com.carbo.fleet.dto.PersonnelDto =====
package com.carbo.fleet.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class PersonnelDto {

    private String id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String secondName;

    @NotEmpty
    private String jobTitle;

    @NotEmpty
    private String employeeId;

    @NotNull
    private Boolean supervisor;

    @NotEmpty
    private String districtId;
    private String districtName;

    private String supervisorId;
    private String supervisorName;

    @NotEmpty
    private String fleetId;
    private String fleetName;

    @NotEmpty
    private String crewId;
    private String crewName;

    private String organizationId;
}

// ===== Imported from: com.carbo.fleet.model.Personnel =====
package com.carbo.fleet.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@Document(collection = "personnel")
@CompoundIndex(def = "{'organizationId': 1, 'employeeId':1}", name = "organization_employee_index", unique = true)
public class Personnel {
    @Id
    private String id;

    @Field("firstName")
    private String firstName;

    @Field("secondName")
    private String secondName;

    @Field("jobTitle")
    private String jobTitle;

    @Field("employeeId")
    private String employeeId;

    @Field("districtId")
    private String districtId;

    @Field("supervisor")
    private Boolean supervisor;

    @Field("supervisorId")
    private String supervisorId;

    @Field("fleetId")
    private String fleetId;

    @Field("crewId")
    private String crewId;

    @Field("organizationId")
    private String organizationId;
}

// ===== Imported from: com.carbo.fleet.model.PersonnelDisplay =====
package com.carbo.fleet.model;

import com.carbo.fleet.dto.PersonnelDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PersonnelDisplay {
    private List<PersonnelDto> personnelDisplayObject;
    private Long totalCount;
}

// ===== Imported from: com.carbo.fleet.model.TotalCountObject =====
package com.carbo.fleet.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TotalCountObject {
    private Long totalCount;

    public TotalCountObject(Long totalCount) {
        this.totalCount = totalCount;
    }

    public TotalCountObject() {
    }
}

// ===== Imported from: com.carbo.fleet.repository.PersonnelDBRepository =====
package com.carbo.fleet.repository;

import com.carbo.fleet.model.Personnel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelDBRepository extends MongoRepository<Personnel,String> {
    Optional<List<Personnel>> findByOrganizationId(String organizationId, Pageable pageable);
    Optional<Personnel> findByOrganizationIdAndId(String organizationId,String id);
    Optional<List<Personnel>> findByOrganizationIdAndDistrictId(String organizationId ,String district);
    Optional<List<Personnel>> findByOrganizationIdAndJobTitle(String organizationId, String jobTitle);
    Optional<List<Personnel>> findByOrganizationIdAndDistrictIdAndJobTitle(String organizationId, String district,String jobTitle);
    Optional<List<Personnel>> findByOrganizationIdAndFirstNameAndDistrictIdAndJobTitle(String organizationId,String name,String district,String jobTitle);
    Optional<List<Personnel>> findByOrganizationIdAndSecondNameAndDistrictIdAndJobTitle(String organizationId,String name,String district,String jobTitle);

}

// ===== Current file: src\main\java\com\carbo\fleet\services\PersonnelService.java =====
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

        ProjectionOperation project = Aggregation.project()
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

