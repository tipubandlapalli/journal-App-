package net.engineeringdigest.journalApp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Document(collection = "journal_entries")
public class JournalEntryEntity {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime localDateTime;

    @Override
    public boolean equals(Object o){
        if(o == null || getClass() != o.getClass()) return false;
        JournalEntryEntity other = (JournalEntryEntity) o;
        return getId().equals(other.getId());
    }
}
