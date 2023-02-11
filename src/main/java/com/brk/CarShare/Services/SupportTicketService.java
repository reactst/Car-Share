package com.brk.CarShare.Services;

import com.brk.CarShare.Entities.SupportTicket;
import com.brk.CarShare.Repositories.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportTicketService {
    private final SupportTicketRepository supportTicketRepository;

    public Page<SupportTicket> getAllSupportTicketsPaginated(Pageable pageable){
        return supportTicketRepository.findAll(pageable);
    }

    public void addSupportTicket(SupportTicket supportTicket)
    {
        supportTicketRepository.insert(supportTicket);
    }
    public List<SupportTicket> getAllSupportTickets() {
        return supportTicketRepository.findAll();
    }

    public SupportTicket getSupportTicketById(String id) {
        return supportTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot Find Support ticket by ID - " + id)));
    }
    public void updateSupportTicket(SupportTicket supportTicket) {
        supportTicketRepository.save(supportTicket);
    }
    public List<SupportTicket> getSupportTicketsByDescriptionContaining(String query){
        return supportTicketRepository.getSupportTicketsByDescriptionContaining(query);
    }
    public List<SupportTicket> getSupportTicketsByTicketTimeBetween(String dateFrom, String dateTo){
        return supportTicketRepository.getSupportTicketsByTicketTimeBetween(dateFrom, dateTo);
    }
    public List<SupportTicket> getSupportTicketsByDescriptionContainingAndTicketTimeIsBetween(String query, String dateFrom, String dateTo){
        return supportTicketRepository.getSupportTicketsByDescriptionContainingAndTicketTimeIsBetween(query, dateFrom, dateTo);    }
    public Page<SupportTicket> findSupportTicketsBySearchTerm(String searchTerm, LocalDateTime dateFromLocal, LocalDateTime dateToLocal, Pageable pageable) {
        if(StringUtils.isEmpty(searchTerm)){
            return supportTicketRepository.findAllByTicketTimeBetween(dateFromLocal, dateToLocal, pageable);
        }else {
            return supportTicketRepository.findByTicketTimeBetweenAndTitleContainingIgnoreCaseOrDescriptionContainsIgnoreCase(dateFromLocal, dateToLocal,searchTerm,searchTerm, pageable);
        }
    }
    public SupportTicket createTicket(String userId, String tripId, String title, String description) {
        SupportTicket supportTicket = new SupportTicket(userId, tripId, title, description);
        supportTicketRepository.insert(supportTicket);
        return supportTicket;
    }
}