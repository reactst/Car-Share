package com.brk.CarShare.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "trip")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tripId;
    private String userId;
    private Vehicle vehicle;
    private String status;
    @Length(max = 2)
    private List startLocation;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;
    @Length(max = 2)
    private List endLocation;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

    public Trip(String userId, Vehicle vehicle){
        this.userId=userId;
        this.vehicle = vehicle;
        this.status="Ongoing";
        this.startLocation=vehicle.getLocation();
        this.startTime= LocalDateTime.now();
        this.endTime=null;
        this.endLocation=new ArrayList<Double>();
    }
}
