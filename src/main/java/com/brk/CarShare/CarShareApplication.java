package com.brk.CarShare;


import com.brk.CarShare.Entities.Vehicle;
import com.brk.CarShare.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories
public class CarShareApplication implements CommandLineRunner {

	private final SupportTicketRepository supportTicketRepository;
	private final TripRepository tripRepository;
	private final VehicleRepository vehicleRepository;

	@Autowired
	public CarShareApplication(SupportTicketRepository supportTicketRepository, TripRepository tripRepository, VehicleRepository vehicleRepository){
		this.supportTicketRepository = supportTicketRepository;
		this.tripRepository = tripRepository;
		this.vehicleRepository = vehicleRepository;
	}
	public static void main(String[] args) {
		SpringApplication.run(CarShareApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (vehicleRepository.findAll().isEmpty()) {
			Vehicle vehicle1 = new Vehicle();
			vehicle1.setMake("Tesla");
			vehicle1.setModel("Model 3");
			vehicle1.setFuelType("Electric");
			vehicle1.setModelYear("2020");
			vehicle1.setRegistration("ABC123");
			vehicle1.setVin("123ABC456DEF789GHI");
			vehicle1.setStatus("Available");
			vehicle1.setSubscriptionTier("Premium");
			vehicle1.setLocation(Arrays.asList(43.512115,16.435917));
			vehicle1.setImageUrl("https://tesla-cdn.thron.com/delivery/public/image/tesla/c82315a6-ac99-464a-a753-c26bc0fb647d/bvlatuR/std/1200x628/lhd-model-3-social");
			vehicleRepository.save(vehicle1);

			Vehicle vehicle2 = new Vehicle();
			vehicle2.setMake("Chevrolet");
			vehicle2.setModel("Bolt");
			vehicle2.setFuelType("Electric");
			vehicle2.setModelYear("2019");
			vehicle2.setRegistration("DEF456");
			vehicle2.setVin("456DEF789GHI123ABC");
			vehicle2.setStatus("Unavailable");
			vehicle2.setSubscriptionTier("Standard");
			vehicle2.setLocation(Arrays.asList(43.520164,16.429659));
			vehicle1.setImageUrl("https://media.ed.edmunds-media.com/chevrolet/bolt-ev/2023/oem/2023_chevrolet_bolt-ev_4dr-hatchback_2lt_fq_oem_1_1280.jpg");
			vehicleRepository.save(vehicle2);

			Vehicle vehicle3 = new Vehicle();
			vehicle3.setMake("Nissan");
			vehicle3.setModel("Leaf");
			vehicle3.setFuelType("Electric");
			vehicle3.setModelYear("2018");
			vehicle3.setRegistration("GHI789");
			vehicle3.setVin("789GHI123ABC456DEF");
			vehicle3.setStatus("Available");
			vehicle3.setSubscriptionTier("Basic");
			vehicle3.setLocation(Arrays.asList(43.502668,16.475124));
			vehicle1.setImageUrl("https://www.topgear.com/sites/default/files/2021/10/LEAF10%20SV%20-%20018.jpg");
			vehicleRepository.save(vehicle3);

			System.out.println("SEEDED VEHICLE DATA");
		}
	}
}
