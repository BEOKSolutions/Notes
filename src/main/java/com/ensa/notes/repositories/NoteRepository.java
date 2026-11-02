package com.ensa.notes.repositories;

import com.ensa.notes.entities.Note;
import com.ensa.notes.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByCode(UUID code);
    List<Note> findByLevel(Level level);
    List<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

}
