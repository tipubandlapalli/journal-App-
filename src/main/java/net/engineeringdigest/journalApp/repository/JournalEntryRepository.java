package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JournalEntryRepository extends MongoRepository<JournalEntryEntity,String> {
    List<JournalEntryEntity> findBy(Pageable pageable);
}
