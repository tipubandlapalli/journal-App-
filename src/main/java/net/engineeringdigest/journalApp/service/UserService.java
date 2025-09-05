package net.engineeringdigest.journalApp.service;

import java.util.List;
import java.util.Optional;
import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalEntryService journalEntryService;

    public boolean exitsByUsername(String username){
       return userRepository.existsByUsername(username);
    }

    public boolean existsById(String id){
        return userRepository.existsById(id);
    }

    public UserEntity getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void saveUser(UserEntity userEntity){
        userEntity.setUsername(userEntity.getUsername().trim());
        userEntity.setPassword(userEntity.getPassword().trim());
        userRepository.save(userEntity);
    }

    public List<UserEntity> getTenUsers() {
        Pageable pageable = PageRequest.of(0,10);
        return userRepository.findBy(pageable);
    }

    public void deleteUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        List<JournalEntryEntity> list = userEntity.getJournalEntryList();
        for(JournalEntryEntity journalEntry: list) {
            journalEntryService.deleteEntry(journalEntry.getId());
        }
        userRepository.deleteById(userEntity.getId());
    }

    public Optional<UserEntity>  editById(String id, UserEntity userEntity) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if(!userEntityOptional.isPresent()) return userEntityOptional;

        if(userEntity.getPassword().trim().isEmpty()) userEntity.setPassword(userEntityOptional.get().getPassword());
        if(userEntity.getUsername().trim().isEmpty()) userEntity.setUsername(userEntityOptional.get().getUsername());
        if(userEntity.getEmail() == null || userEntity.getEmail().trim().isEmpty()) userEntity.setEmail(userEntityOptional.get().getEmail());
        userEntity.setJournalEntryList(userEntityOptional.get().getJournalEntryList());
        userRepository.save(userEntity);
        return userRepository.findById(userEntity.getId());
    }
}
