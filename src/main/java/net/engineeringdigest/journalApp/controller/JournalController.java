package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalController {

    protected Map<Long,JournalEntry> journalEntries = new HashMap<>();

    @GetMapping("id/{myId}")
    public  ResponseEntity<?>  getJournalEntry(@PathVariable Long myId){
        if(!journalEntries.containsKey(myId)){
            return new ResponseEntity<>("Journal Entry Not Found !!!!!!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(journalEntries.get(myId),HttpStatus.ACCEPTED);
    }

    @GetMapping
    public List<JournalEntry> getAllJournals(){
        return new LinkedList<>(journalEntries.values());
    }

    @PostMapping
    public ResponseEntity<?>  postJournalEntry(@RequestBody JournalEntry journalEntry){
        if(journalEntries.containsKey(journalEntry.getId())){
            return new ResponseEntity<>("Duplicate ID",HttpStatus.BAD_GATEWAY);
        }
        journalEntries.put(journalEntry.getId(), journalEntry);
        return new ResponseEntity<>(journalEntries.get(journalEntry.getId()), HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<?> editJournalEntry(@RequestBody JournalEntry journalEntry){
        if(!journalEntries.containsKey(journalEntry.getId())){
            return new ResponseEntity<>("Journal Entry Not Found !!!!!!",HttpStatus.BAD_REQUEST);
        }
        journalEntries.put(journalEntry.getId(),journalEntry);
        return new ResponseEntity<>(journalEntries.get(journalEntry.getId()),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("id/{myId}")
    public  ResponseEntity<?> deleteJournalEntry(@PathVariable Long myId){
        if(!journalEntries.containsKey(myId)){
            return new ResponseEntity<>("Journal Entry Not Found !!!!!!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(journalEntries.remove(myId),HttpStatus.ACCEPTED);
    }
}
