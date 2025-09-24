package com.carbo.fleet.repository;

import com.carbo.fleet.model.Crew;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewDbRepository extends MongoRepository<Crew, String> {
    Optional<Crew> findById(String organizationId, String id);

    Optional<List<Crew>> findByOrganizationIdAndFleetId(String organiztionId, String fleet, Pageable Pageable);

    Optional<List<Crew>> findByOrganizationId(String organizationId, Pageable pageable);
}
