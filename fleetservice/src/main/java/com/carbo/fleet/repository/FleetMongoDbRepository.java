package com.carbo.fleet.repository;

import com.carbo.fleet.model.Fleet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FleetMongoDbRepository extends MongoRepository<Fleet, String> {
    List<Fleet> findByOrganizationId(String organizationId);

    Optional<Fleet> findDistinctByOrganizationIdAndName(String organizationId, String name);
//    long countByTypeAndFleet(String equipmentType, String fleet);
//
//    long countByTypeInAndFleet(List<String> equipmentTypes, String fleet);
//
//        Fleet findByFleetId(String fleetId);

    List<Fleet> findByOrganizationIdAndIdIn(String organizationId, Set<String> fleetIds);

    List<Fleet> findByOrganizationIdInAndNameIn(Set<String> organizationIds, Set<String> filterFleetName);
}



