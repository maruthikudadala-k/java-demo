package com.carbo.fleet.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.carbo.fleet.model.Job;

@Repository
public interface JobMongoDbRepository extends MongoRepository<Job, String> {
    
//    List<Job> findBySharedWithOrganizationIdAndStatus(String organizationId, String completed);

    List<Job> findBySharedWithOrganizationIdAndStatus(String organizationId, String status);
}
