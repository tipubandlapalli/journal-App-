package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
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
        Optional<JournalEntryEntity> entryOptional = journalEntryService.getJournalEntryOById(id);
        if(!entryOptional.isPresent()) {
            return new ResponseEntity<>("Journal Entry not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entryOptional.get(),HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<?> getAllJournals(){
        return new ResponseEntity<>(journalEntryService.getTenEntries(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?>  postJournalEntry(@RequestBody JournalEntryEntity journalEntryEntity){
        if(journalEntryEntity.getId() != null) {
            return new ResponseEntity<>("You can't set id",HttpStatus.BAD_REQUEST);
        }
        journalEntryEntity.setLocalDateTime(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntryEntity);
        return new ResponseEntity<>(journalEntryService.getJournalEntryOById(journalEntryEntity.getId()).get(),HttpStatus.CREATED);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> editJournalEntry(@RequestBody JournalEntryEntity journalEntryEntity, @PathVariable String id){
        journalEntryEntity.setId(id);
        if(!journalEntryService.editEntry(journalEntryEntity)) {
            return new ResponseEntity<>("Journal Entry not found with the ID",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(journalEntryService.getJournalEntryOById(journalEntryEntity.getId()).get(),HttpStatus.OK);
    }

    @DeleteMapping("id/{id}")
    public  ResponseEntity<?> deleteJournalEntry(@PathVariable String id){
        if(!journalEntryService.doesEntryExist(id)) {
            return new ResponseEntity<>("Journal Entry not found",HttpStatus.NOT_FOUND);
        }
        journalEntryService.deleteEntry(id);
        return new ResponseEntity<>("Entry DELETED",HttpStatus.NO_CONTENT);
    }
}
