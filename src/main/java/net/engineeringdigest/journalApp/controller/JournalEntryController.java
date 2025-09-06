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

    @GetMapping("{username}/{id}")
    public  ResponseEntity<?>  getJournalEntry(
            @PathVariable String id,
            @PathVariable String username
    ){
        Optional<JournalEntryEntity> entryOptional = journalEntryService.getJournalEntryOById(id);
        if(!entryOptional.isPresent()) {
            return new ResponseEntity<>("Journal Entry not found",HttpStatus.NOT_FOUND);
        }
        if(!journalEntryService.isOwnershipCorrect(username,id)){
            return new ResponseEntity<>("You are not authorized",HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(entryOptional.get(),HttpStatus.OK);
    }

    @GetMapping("{username}")
    public  ResponseEntity<?> getAllJournalsOfUser(@PathVariable String username){
        if(!journalEntryService.isUserExistsByUsername(username)){
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(journalEntryService.getAllJournalsOfUser(username),HttpStatus.OK);
    }

    @PostMapping("{username}")
    public ResponseEntity<?>  postJournalEntry(
            @RequestBody JournalEntryEntity journalEntryEntity,
            @PathVariable String username
    ){
        if(!journalEntryService.isUserExistsByUsername(username)){
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        if(journalEntryEntity.getId() != null) {
            return new ResponseEntity<>("You can't set id",HttpStatus.BAD_REQUEST);
        }
        journalEntryEntity.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(journalEntryService.saveEntry(journalEntryEntity,username),HttpStatus.CREATED);
    }

    @PutMapping("{username}/{id}")
    public ResponseEntity<?> editJournalEntry(
            @RequestBody JournalEntryEntity journalEntryEntity,
            @PathVariable String id,
            @PathVariable String username
    ){
        if(!journalEntryService.isUserExistsByUsername(username)){
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        if(!journalEntryService.doesEntryExist(id)){
            return new ResponseEntity<>("Journal Entry not found with the ID",HttpStatus.NOT_FOUND);
        }
        if(journalEntryEntity.getId() != null) {
            return new ResponseEntity<>("You can't set id",HttpStatus.BAD_REQUEST);
        }
        if(!journalEntryService.isOwnershipCorrect(username,id)){
            return new ResponseEntity<>("You are not Authorized",HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(journalEntryService.editEntry(journalEntryEntity,username,id), HttpStatus.OK);
    }

    @DeleteMapping("{username}/{id}")
    public  ResponseEntity<?> deleteJournalEntry(
            @PathVariable String id,
            @PathVariable String username
    ){
        if(!journalEntryService.isUserExistsByUsername(username)){
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        if(!journalEntryService.doesEntryExist(id)){
            return new ResponseEntity<>("Journal Entry not found with the ID",HttpStatus.NOT_FOUND);
        }
        if(!journalEntryService.deleteEntry(id,username)){
            return new ResponseEntity<>("You are not Authorized",HttpStatus.UNAUTHORIZED);
        };
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
