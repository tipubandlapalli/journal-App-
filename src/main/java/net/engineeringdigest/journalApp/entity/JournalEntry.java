package net.engineeringdigest.journalApp.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@Document(collection = "journal_entries")
public class JournalEntry {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime localDateTime;
}
