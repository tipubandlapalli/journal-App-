package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JournalEntryRepository extends MongoRepository<JournalEntry,String> {
    List<JournalEntry> findBy(Pageable pageable);

}
