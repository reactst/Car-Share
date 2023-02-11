package com.brk.CarShare.Services;

import com.brk.CarShare.Entities.Vehicle;
import com.brk.CarShare.Repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public void addVehicle(Vehicle vehicle)
    {
        vehicleRepository.insert(vehicle);
    }
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleList.stream().filter(vehicle -> Objects.equals(vehicle.getStatus(), "Available")).collect(Collectors.toList());
    }

    public Page<Vehicle> getAllVehiclesPaginated(Pageable pageable){
        return vehicleRepository.findAll(pageable);
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot Find Vehicle by ID - %s", id)));
    }
    public void updateVehicle(Vehicle vehicle) {
        Vehicle savedVehicle = vehicleRepository.findById(vehicle.getId()).orElseThrow(() -> new RuntimeException(String.format("Cannot Find Vehicle by ID %s", vehicle.getId())));
        savedVehicle.setId(vehicle.getId());
        savedVehicle.setMake(vehicle.getMake());
        savedVehicle.setModel(vehicle.getModel());

        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(String id) {
        vehicleRepository.deleteById(id);
    }
    public List<Vehicle> getVehiclesByLocation(List<Double> coordinates, int distance){
        List<Vehicle> savedVehicles = vehicleRepository.findAll();
        return savedVehicles.stream().filter(vehicle -> distance<sqrt(pow(vehicle.getLocation().get(0) - coordinates.get(0), 2) + pow(vehicle.getLocation().get(1) - coordinates.get(1), 2) )).collect(Collectors.toList());
    }
    public Page<Vehicle> findBySearchTerm(String searchTerm, int resultLimit, Pageable pageable) {
        if(StringUtils.isEmpty(searchTerm)){
            return vehicleRepository.findAll(pageable);
        }else {
            return vehicleRepository.findByMakeContainingIgnoreCaseOrModelContainingIgnoreCase(searchTerm, resultLimit, pageable);
        }
    }

    public Page<Vehicle> findVehiclesBySearchTerm(String searchTerm, Pageable pageable) {
        if (StringUtils.isEmpty(searchTerm)) {
            return vehicleRepository.findAll(pageable);
        } else {
            return vehicleRepository.findByAllAttributesContainingIgnoreCase(searchTerm, pageable);
        }
    }
}
