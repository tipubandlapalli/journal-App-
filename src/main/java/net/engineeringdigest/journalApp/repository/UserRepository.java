package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends MongoRepository<UserEntity,String> {
    boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
    void deleteByUsername(String username);
    List<UserEntity> findBy(Pageable pageable);
}
