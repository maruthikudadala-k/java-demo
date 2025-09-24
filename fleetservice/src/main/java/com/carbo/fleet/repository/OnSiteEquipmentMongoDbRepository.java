package com.carbo.fleet.repository;

import com.carbo.fleet.model.OnSiteEquipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OnSiteEquipmentMongoDbRepository extends MongoRepository<OnSiteEquipment,String> {
    List<OnSiteEquipment> findByFleetIdIn(Set<String> fleetIds);
}
