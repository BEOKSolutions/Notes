package com.ensa.notes.config;

import com.ensa.notes.entities.Note;
import com.ensa.notes.enums.Level;
import com.ensa.notes.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer {

    private final NoteRepository noteRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
                if(noteRepository.count() == 0) {
                    List<Note> initialNotes = List.of(
                            Note.builder().title("Note 1").content("Note 1 content").level(Level.LOW).build(),
                            Note.builder().title("Note 2").content("Note 2 content").level(Level.MEDIUM).build(),
                            Note.builder().title("Note 3").content("Note 3 content").level(Level.HIGH).build()
                    );
                    List<Note>savedNotes = noteRepository.saveAll(initialNotes);
                    log.info("==== Notes saved: {} into H2 ===", savedNotes.size());
                    savedNotes.forEach(note ->
                            log.info("Note created with infos:  {} - {} - {}", note.getCode(), note.getTitle(), note.getContent())
                    );
                }
        };
    }
}
