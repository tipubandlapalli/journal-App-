package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public boolean doesEntryExist(String id){
        return journalEntryRepository.existsById(id);
    }

    public Optional<JournalEntry> getJournalEntryOById(String id){
        return journalEntryRepository.findById(id);
    }

    public void saveEntry(JournalEntry entry){
        journalEntryRepository.save(entry);
    }

    public boolean editEntry(JournalEntry entry){
        Optional<JournalEntry> entryExists = journalEntryRepository.findById(entry.getId());
        if(!entryExists.isPresent()) return false;
        if(entry.getTitle() == null || entry.getTitle().trim().isEmpty()) entry.setTitle(entryExists.get().getTitle());
        if(entry.getContent() == null || entry.getContent().trim().isEmpty()) entry.setContent(entryExists.get().getContent());
        saveEntry(entry);
        return true;
    }
    public void deleteEntry(String id){
        journalEntryRepository.deleteById(id);
    }

    public List<JournalEntry> getTenEntries(){
        Pageable pageable = PageRequest.of(1,10);
        return journalEntryRepository.findBy(pageable);
    }
}
