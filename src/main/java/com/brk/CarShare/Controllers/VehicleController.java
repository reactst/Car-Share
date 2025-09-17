package com.brk.CarShare.Controllers;

import com.brk.CarShare.Entities.Vehicle;
import com.brk.CarShare.Services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    @Autowired
    private final VehicleService vehicleService;

    @GetMapping("")
    public ResponseEntity<Page<Vehicle>> findVehiclesBySearchTerm(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Pageable pageable) {
        Page<Vehicle> vehicles = vehicleService.findVehiclesBySearchTerm(searchTerm, pageable);
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.addVehicle(vehicle);
        return ResponseEntity.ok("Successfully added vehicle!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.updateVehicle(vehicle);
        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteVehicle(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getVehiclesInRange")
    public List<Vehicle> getVehiclesByLocation(List<Double> coordinates, int distance){
        return vehicleService.getVehiclesByLocation(coordinates, distance);
    }

}
