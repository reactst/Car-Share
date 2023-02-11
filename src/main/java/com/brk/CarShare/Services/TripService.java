package com.brk.CarShare.Services;

import com.brk.CarShare.Entities.Trip;
import com.brk.CarShare.Entities.Vehicle;
import com.brk.CarShare.Repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService {
    private final TripRepository tripRepository;

    public Page<Trip> getAllTripsPaginated(Pageable pageable){
        return tripRepository.findAll(pageable);
    }
    public void addTrip(Trip trip){tripRepository.insert(trip);}
    public Trip startTrip(String userId, Vehicle vehicle){
        Trip newTrip = new Trip(userId, vehicle);
        tripRepository.insert(newTrip);
        return newTrip;
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }
    public Trip getTripById(String id){
        return tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find trip by ID"));
    }
    public List<Trip> getTripsByStartTimeBetween(String dateFrom, String dateTo){
        return tripRepository.getTripsByStartTimeBetween(dateFrom, dateTo);
    }


    public Trip endTrip(Trip trip, List<Double> endLocation) {
        trip.setEndLocation(endLocation);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        trip.setEndTime(now);
        trip.setStatus("Ended");
        tripRepository.save(trip);
        return trip;
    }

    public Page<Trip> findTripsByDate(LocalDateTime dateFrom, LocalDateTime dateTo, String userId, Pageable pageable) {
        if (dateFrom == null || dateTo == null) {
            return tripRepository.findAll(pageable);
        } else {
            return tripRepository.findByUserIdAndStartTimeBetween(userId,dateFrom, dateTo, pageable);
        }
    }
}
