package com.brk.CarShare.Repositories;

import com.brk.CarShare.Entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    @Query("{$or:[{'make':{$regex:?0,$options:'i'}},{'model':{$regex:?0,$options:'i'}},{'fuelType':{$regex:?0,$options:'i'}},{'modelYear':{$regex:?0,$options:'i'}},{'registration':{$regex:?0,$options:'i'}},{'vin':{$regex:?0,$options:'i'}},{'status':{$regex:?0,$options:'i'}},{'subscriptionTier':{$regex:?0,$options:'i'}}]}")
    Page<Vehicle> findByAllAttributesContainingIgnoreCase(String searchTerm, Pageable pageable);

    Page<Vehicle> findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(String searchTerm, int resultLimit, Pageable pageable);
}
