package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("id/{id}")
    public  ResponseEntity<?>  getJournalEntry(@PathVariable String id){
        Optional<JournalEntry> entryOptional = journalEntryService.getJournalEntryOById(id);
        if(!entryOptional.isPresent()) {
            return new ResponseEntity<>("Journal Entry not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(entryOptional.get(),HttpStatus.ACCEPTED);
    }

    @GetMapping
    public  ResponseEntity<?> getAllJournals(){
        return new ResponseEntity<>(journalEntryService.getTenEntries(),HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<?>  postJournalEntry(@RequestBody JournalEntry journalEntry){
        if(journalEntry.getId() != null) {
            return new ResponseEntity<>("You can't set id",HttpStatus.BAD_REQUEST);
        }
        journalEntry.setLocalDateTime(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return new ResponseEntity<>(journalEntryService.getJournalEntryOById(journalEntry.getId()).get(),HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<?> editJournalEntry(@RequestBody JournalEntry journalEntry){
        if(!journalEntryService.editEntry(journalEntry)) {
            return new ResponseEntity<>("Journal Entry not found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(journalEntryService.getJournalEntryOById(journalEntry.getId()).get(),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("id/{id}")
    public  ResponseEntity<?> deleteJournalEntry(@PathVariable String id){
        if(!journalEntryService.doesEntryExist(id)) {
            return new ResponseEntity<>("Journal Entry not found",HttpStatus.BAD_REQUEST);
        }
        journalEntryService.deleteEntry(id);
        return new ResponseEntity<>("Entry DELETED",HttpStatus.ACCEPTED);
    }
}
