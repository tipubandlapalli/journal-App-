package net.engineeringdigest.journalApp.service;

import java.util.List;
import java.util.Optional;
import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
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
    private JournalEntryRepository journalEntryRepository;

    public boolean exitsByUsername(String username){
       return userRepository.existsByUsername(username);
    }

    public boolean existsById(String id){
        return userRepository.existsById(id);
    }

    public UserEntity getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public UserEntity saveUser(UserEntity userEntity){
        userEntity.setUsername(userEntity.getUsername().trim());
        userEntity.setPassword(userEntity.getPassword().trim());
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getTenUsers() {
        Pageable pageable = PageRequest.of(0,10);
        return userRepository.findBy(pageable);
    }

    public void deleteUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        List<JournalEntryEntity> list = userEntity.getJournalEntryList();
        for(JournalEntryEntity journalEntry: list) {
            journalEntryRepository.deleteById(journalEntry.getId());
        }
        userRepository.deleteByUsername(userEntity.getUsername());
    }

    public UserEntity editByUsername(String username, UserEntity userEntity) {

        UserEntity userEntityExists = userRepository.findByUsername(username);

        boolean[] is = {
                !userEntity.getUsername().trim().isEmpty(),
                !userEntity.getPassword().trim().isEmpty(),
                userEntity.getEmail() != null && !userEntity.getEmail().trim().isEmpty()
        };
        String[] val = {
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail()
        };
        if(is[0]) userEntityExists.setUsername(val[0]);
        if(is[1]) userEntityExists.setPassword(val[1]);
        if(is[2]) userEntityExists.setEmail(val[2]);

        return userRepository.save(userEntityExists);
    }
}
