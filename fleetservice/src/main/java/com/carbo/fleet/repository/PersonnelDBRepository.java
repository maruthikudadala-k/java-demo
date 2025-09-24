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
