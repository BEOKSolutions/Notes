package com.ensa.notes.controllers;

import com.ensa.notes.config.kafka.NoteProducer;
import com.ensa.notes.dtos.NoteRequest;
import com.ensa.notes.dtos.NoteResponse;
import com.ensa.notes.enums.Level;
import com.ensa.notes.models.HttpResponse;
import com.ensa.notes.services.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteProducer noteProducer;

    @GetMapping
    public ResponseEntity<HttpResponse<NoteResponse>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/{code}")
    public ResponseEntity<HttpResponse<NoteResponse>> getNoteByCode(@PathVariable UUID code) {
        return ResponseEntity.ok(noteService.getNoteByCode(code));
    }

    @GetMapping("/filter")
    public ResponseEntity<HttpResponse<NoteResponse>> getNoteByLevel(@RequestParam Level level) {
        return ResponseEntity.ok(noteService.getNoteByLevel(level));
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse<NoteResponse>> searchNotes(@RequestParam String search) {
        return ResponseEntity.ok(noteService.searchNotes(search));
    }

    @PostMapping
    public ResponseEntity<HttpResponse<NoteResponse>> createNote(
            @Valid
            @RequestBody
            NoteRequest noteRequest)
    {

        noteProducer.send(noteRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteService.createNote(noteRequest));
    }

    @PutMapping("/{code}")
    public ResponseEntity<HttpResponse<NoteResponse>> updateNote(
            @PathVariable UUID code,
            @Valid
            @RequestBody
            NoteRequest noteRequest)
        {
        return ResponseEntity.ok(noteService.updateNote(code, noteRequest));
        }

    @DeleteMapping("/{code}")
    public ResponseEntity<HttpResponse<NoteResponse>> deleteNote(@PathVariable UUID code) {
       return ResponseEntity.ok(noteService.deleteNote(code));
       }
}
