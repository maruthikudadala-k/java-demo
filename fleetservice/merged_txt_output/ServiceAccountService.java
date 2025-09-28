// ===== Imported from: com.carbo.fleet.model.ServiceAccount =====
package com.carbo.fleet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "service-accounts")
public class ServiceAccount {
    @Id
    private String id;

    @Field("userId")
    private String userId;

    @Field("organizationIds")
    private List<String> organizationIds = new ArrayList<>();

    @Field("microservices")
    private List<String> microservices = new ArrayList<>();

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified  = new Date().getTime();

    @Field("ts")
    private Long ts;

    @Field("organizationId")
    private String organizationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<String> getMicroservices() {
        return microservices;
    }

    public void setMicroservices(List<String> microservices) {
        this.microservices = microservices;
    }

    public Long getCreated() {
        return created;
    }
}

// ===== Imported from: com.carbo.fleet.repository.ServiceAccountMongoDbRepository =====
package com.carbo.fleet.repository;

import com.carbo.fleet.model.ServiceAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAccountMongoDbRepository extends MongoRepository<ServiceAccount, String> {
    List<ServiceAccount> findByOrganizationId(String organizationId);
}

// ===== Current file: src\main\java\com\carbo\fleet\services\ServiceAccountService.java =====
package com.carbo.fleet.services;

import com.carbo.fleet.model.ServiceAccount;
import com.carbo.fleet.repository.ServiceAccountMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceAccountService {
    private final ServiceAccountMongoDbRepository serviceAccountMongoDbRepository;

    @Autowired
    public ServiceAccountService(ServiceAccountMongoDbRepository serviceAccountMongoDbRepository) {
        this.serviceAccountMongoDbRepository = serviceAccountMongoDbRepository;
    }

    public List<ServiceAccount> getAll() {
        return serviceAccountMongoDbRepository.findAll();
    }

    public List<ServiceAccount> getByOrganizationId(String organizationId) {
        return serviceAccountMongoDbRepository.findByOrganizationId(organizationId);
    }

    public Optional<ServiceAccount> get(String serviceAccountId) {
        return serviceAccountMongoDbRepository.findById(serviceAccountId);
    }

    public ServiceAccount save(ServiceAccount serviceAccount) {
        return serviceAccountMongoDbRepository.save(serviceAccount);
    }

    public void update(ServiceAccount serviceAccount) {
        serviceAccountMongoDbRepository.save(serviceAccount);
    }

    public void delete(String serviceAccountId) {
        serviceAccountMongoDbRepository.deleteById(serviceAccountId);
    }
}

