package com.brk.CarShare.Controllers;

import com.brk.CarShare.Entities.SupportTicket;
import com.brk.CarShare.Entities.Trip;
import com.brk.CarShare.Entities.Vehicle;
import com.brk.CarShare.Services.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/supportticket")
@RequiredArgsConstructor
public class SupportTicketController {

    @Autowired
    private final SupportTicketService supportTicketService;
    @PostMapping("/add")
    public ResponseEntity<String> addSupportTicket(@RequestBody SupportTicket supportTicket) {
        supportTicketService.addSupportTicket(supportTicket);
        return ResponseEntity.ok("Succesfully added support ticket!");
    }

    @GetMapping("")
    public Page<SupportTicket> findSupportTicketsBySearchTerm(@RequestParam(required = false) String searchTerm,@RequestParam(required = false) String dateFrom,@RequestParam(required = false) String dateTo, Pageable pageable) {
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateFromLocal = LocalDateTime.parse(dateFrom, dtf);
        LocalDateTime dateToLocal = LocalDateTime.parse(dateTo, dtf);
        return supportTicketService.findSupportTicketsBySearchTerm(searchTerm,dateFromLocal, dateToLocal, pageable);
    }


    @GetMapping("/all")
    public ResponseEntity<Page<SupportTicket>> getAllSupportTicketsPaginated(Pageable pageable) {
        return ResponseEntity.ok(supportTicketService.getAllSupportTicketsPaginated(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getSupportTicketById(@PathVariable String id) {
        return ResponseEntity.ok(supportTicketService.getSupportTicketById(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<SupportTicket> updateSupportTicket(@RequestBody SupportTicket supportTicket) {
        supportTicketService.updateSupportTicket(supportTicket);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/findByDescription")
    public ResponseEntity<List<SupportTicket>> getSupportTicketsByDescriptionContaining(String query){
        return ResponseEntity.ok(supportTicketService.getSupportTicketsByDescriptionContaining(query));
    }
    @GetMapping("/findByDate")
    public ResponseEntity<List<SupportTicket>> getSupportTicketsByTicketTimeBetween(String dateFrom, String dateTo){
        return ResponseEntity.ok(supportTicketService.getSupportTicketsByTicketTimeBetween(dateFrom, dateTo));
    }
    @GetMapping("/findByDescriptionDate")
    public ResponseEntity<List<SupportTicket>> getSupportTicketsByDescriptionContainingAndTicketTimeIsBetween(String query, String dateFrom, String dateTo){
        return ResponseEntity.ok(supportTicketService.getSupportTicketsByDescriptionContainingAndTicketTimeIsBetween(query, dateFrom, dateTo));
    }

    @PostMapping("/create")
    public ResponseEntity createTicket(@RequestParam String userId, @RequestParam String tripId, @RequestParam String title, @RequestParam String description) {
        SupportTicket ticket = supportTicketService.createTicket(userId, tripId, title, description);
        return ResponseEntity.ok(ticket);
    }
}
