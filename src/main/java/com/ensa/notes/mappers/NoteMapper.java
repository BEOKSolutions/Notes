package com.ensa.notes.mappers;

import com.ensa.notes.dtos.NoteRequest;
import com.ensa.notes.dtos.NoteResponse;
import com.ensa.notes.entities.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public Note toEntity(NoteRequest noteRequest) {
        return Note.builder()
                .title(noteRequest.getTitle())
                .content(noteRequest.getContent())
                .level(noteRequest.getLevel())
                .build();
    }

    public NoteResponse toResponse(Note note) {
        return NoteResponse.builder()
                .code(note.getCode())
                .title(note.getTitle())
                .content(note.getContent())
                .level(note.getLevel())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }
}
