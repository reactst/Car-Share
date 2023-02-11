package com.brk.CarShare.Repositories;

import com.brk.CarShare.Entities.SupportTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SupportTicketRepository extends MongoRepository<SupportTicket, String> {
    List<SupportTicket> getSupportTicketsByDescriptionContaining(String query);
    List<SupportTicket> getSupportTicketsByTicketTimeBetween(String dateFrom, String dateTo);
    List<SupportTicket> getSupportTicketsByDescriptionContainingAndTicketTimeIsBetween(String query, String dateFrom, String dateTo);

    Page<SupportTicket> findByTicketTimeBetweenAndTitleContainingIgnoreCaseOrDescriptionContainsIgnoreCase(LocalDateTime ticketTime, LocalDateTime ticketTime2, String title, String description, Pageable pageable);

    Page<SupportTicket> findAllByTicketTimeBetween(LocalDateTime dateFromLocal, LocalDateTime dateToLocal, Pageable pageable);
}
