package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;
    
    public boolean doesEntryExist(String id){
        return journalEntryRepository.existsById(id);
    }

    public Optional<JournalEntryEntity> getJournalEntryOById(String id){
        return journalEntryRepository.findById(id);
    }

    public void saveEntry(JournalEntryEntity entry,String username){
        UserEntity userEntity = userRepository.findByUsername(username);

        JournalEntryEntity saved = journalEntryRepository.save(entry);

        userEntity.getJournalEntryList().add(entry);
        userRepository.save(userEntity);
    }
    public void saveEntry(JournalEntryEntity entry){
        journalEntryRepository.save(entry);
    }

    public boolean editEntry(JournalEntryEntity entry, String username){
        UserEntity userEntity = userRepository.findByUsername(username);

        Optional<JournalEntryEntity> entryExists = journalEntryRepository.findById(entry.getId());

        if(!entryExists.isPresent() || !userEntity.getJournalEntryList().contains(entryExists.get())) return false;

        boolean title = entry.getTitle() != null && entry.getTitle().trim().isEmpty();
        boolean content = entry.getContent() != null && entry.getContent().trim().isEmpty();
        if(title) {
            entryExists.get().setTitle(entry.getTitle());
        }
        if(content) {
            entryExists.get().setContent(entry.getContent());
        }
        saveEntry(entry);
        return true;
    }
    public boolean deleteEntry(String id,String username){
        UserEntity userEntity = userRepository.findByUsername(username);
        if(!userEntity.getJournalEntryList().removeIf(x -> x.getId().equals(id))) return false;
        journalEntryRepository.deleteById(id);
        return true;
    }

    public List<JournalEntryEntity> getAllJournalsOfUser(String username) {
        return userRepository.findByUsername(username).getJournalEntryList();
    }
    
    public boolean isUserExistsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

//    public List<JournalEntryEntity> getTenEntries(){
//        Pageable pageable = PageRequest.of(0,10);
//        return journalEntryRepository.findBy(pageable);
//    }

}
