package com.ensa.notes.services;

import com.ensa.notes.dtos.NoteRequest;
import com.ensa.notes.dtos.NoteResponse;
import com.ensa.notes.enums.Level;
import com.ensa.notes.models.HttpResponse;

import java.util.UUID;

public interface NoteService {
    HttpResponse<NoteResponse>getAllNotes();
    HttpResponse<NoteResponse> getNoteByCode(UUID code);
    HttpResponse<NoteResponse> getNoteByLevel(Level level);
    HttpResponse<NoteResponse> searchNotes(String keyword);
    HttpResponse<NoteResponse> createNote(NoteRequest noteRequest);
    HttpResponse<NoteResponse> updateNote(UUID code, NoteRequest noteRequest);
    HttpResponse<NoteResponse> deleteNote(UUID code);
}
