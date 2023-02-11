package com.brk.CarShare.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Document(collection = "supportTicket")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String title;
    private String description;
    private Boolean isClosed;
    private String status;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ticketTime;
    private String userId;
    private String tripId;

    public SupportTicket(String userId, String tripId, String title, String description){
        this.title=title;
        this.description = description;
        this.isClosed=false;
        this.status="Active";
        this.ticketTime = LocalDateTime.now();
        this.userId=userId;
        this.tripId=tripId;
    }
}
