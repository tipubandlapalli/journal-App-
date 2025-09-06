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

    public JournalEntryEntity saveEntry(JournalEntryEntity entry,String username){
        UserEntity userEntity = userRepository.findByUsername(username);

        JournalEntryEntity saved = journalEntryRepository.save(entry);

        userEntity.getJournalEntryList().add(saved);
        userRepository.save(userEntity);
        return saved;
    }
    public void saveEntry(JournalEntryEntity entry){
        journalEntryRepository.save(entry);
    }

    public JournalEntryEntity editEntry(JournalEntryEntity entry, String username, String entryId){

        JournalEntryEntity entryExists = journalEntryRepository.findById(entryId).get();

        boolean title = entry.getTitle() != null && !entry.getTitle().trim().isEmpty();
        boolean content = entry.getContent() != null && !entry.getContent().trim().isEmpty();

        if(title) {
            entryExists.setTitle(entry.getTitle());
        }
        if(content) {
            entryExists.setContent(entry.getContent());
        }
        return journalEntryRepository.save(entry);
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

    public boolean isOwnershipCorrect(String username, String id) {
        UserEntity userEntity = userRepository.findByUsername(username);

        JournalEntryEntity temp = new JournalEntryEntity();
        temp.setId(id);

        return userEntity.getJournalEntryList().contains(temp);
    }

//    public List<JournalEntryEntity> getTenEntries(){
//        Pageable pageable = PageRequest.of(0,10);
//        return journalEntryRepository.findBy(pageable);
//    }

}
