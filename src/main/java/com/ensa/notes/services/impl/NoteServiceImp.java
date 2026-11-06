package com.ensa.notes.services.impl;

import com.ensa.notes.dtos.NoteRequest;
import com.ensa.notes.dtos.NoteResponse;
import com.ensa.notes.entities.Note;
import com.ensa.notes.enums.Level;
import com.ensa.notes.exceptions.NoteNotFoundException;
import com.ensa.notes.mappers.NoteMapper;
import com.ensa.notes.models.HttpResponse;
import com.ensa.notes.repositories.NoteRepository;
import com.ensa.notes.services.NoteService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NoteServiceImp implements NoteService  {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    // builderResponse to remove duplicate Code
    private HttpResponse<NoteResponse> buildResponse(List<NoteResponse> data,
                                                     String message,
                                                     HttpStatus status) {
        return HttpResponse.<NoteResponse>builder()
                .timeStamp(LocalDateTime.now().format(FORMATTER))
                .statusCode(status.value())
                .httpStatus(status)
                .message(message)
                .data(data)
                .build();
    }

    private Note findNoteByCodeOrThrow(UUID code){
        return noteRepository.findByCode(code)
                .orElseThrow(()-> new NoteNotFoundException("Note not found!!"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value ="notesList", key= "'all'")
    public HttpResponse<NoteResponse> getAllNotes() {
        log.info("CACHE MISS -> Get all notes from H2");

        List <NoteResponse> responses = noteRepository
                .findAll()
                .stream()
                .map(noteMapper::toResponse)
                .toList();

        return buildResponse(responses, responses.size()+ " notes found", HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "noteByCode", key = "#code")
    public HttpResponse<NoteResponse> getNoteByCode(UUID code) {
        log.info(" Cache Missing  -> Getting note by code {} from Database", code);

        Note note = findNoteByCodeOrThrow(code);
        NoteResponse noteResponse = noteMapper.toResponse(note);

        return buildResponse(Collections.singletonList(noteResponse), "Note found successfully" , HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "notesList", key = "'level_' + #level.name()")
    public HttpResponse<NoteResponse> getNoteByLevel(Level level) {
        log.info("Cache Missing -> Getting note by level {} from database", level);

        List<NoteResponse> response = noteRepository.findByLevel(level)
                .stream()
                .map(noteMapper::toResponse)
                .toList();

        return buildResponse(response, response.size() + " notes with level "+ level, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "notesList", key = "'search_' + #keyword.toLowerCase()")
    public HttpResponse<NoteResponse> searchNotes(String keyword) {
        log.info("Cache missing Getting notes by keyword {} from database", keyword);

        List<NoteResponse> responses = noteRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(noteMapper::toResponse)
                .toList();

        return buildResponse(responses, responses.size() + " notes found", HttpStatus.OK);
    }

    @Override
    @CacheEvict(value = "notesList", allEntries = true)
    public HttpResponse<NoteResponse> createNote(NoteRequest noteRequest) {
        log.info("Creating note whit title {}", noteRequest.getTitle());

        Note noteToSave  = noteMapper.toEntity(noteRequest);
        Note savedNote = noteRepository.save(noteToSave);
        NoteResponse noteResponse = noteMapper.toResponse(savedNote);

        return buildResponse(Collections.singletonList(noteResponse), "Note created successfully" , HttpStatus.CREATED);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "noteByCode", key = "#code"),
            @CacheEvict(value = "notesList", allEntries = true)
    })
    public HttpResponse<NoteResponse> updateNote(UUID code, NoteRequest noteRequest) {
        log.info("Updating note by code {}", code);

        Note existingNote = findNoteByCodeOrThrow(code);

        existingNote.setTitle(noteRequest.getTitle());
        existingNote.setContent(noteRequest.getContent());
        existingNote.setLevel(noteRequest.getLevel());

        Note updatedNote = noteRepository.save(existingNote);
        NoteResponse noteResponse = noteMapper.toResponse(updatedNote);

        return buildResponse(Collections.singletonList(noteResponse), "Note updated successfully" , HttpStatus.OK);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "noteByCode", key = "#code"),
            @CacheEvict(value = "notesList", allEntries = true)
    })
    public HttpResponse<NoteResponse> deleteNote(UUID code) {
        log.info("Deleting note by code {}", code);

        Note existingNote = findNoteByCodeOrThrow(code);
        NoteResponse deletedResponse  = noteMapper.toResponse(existingNote);
        noteRepository.delete(existingNote);

        return buildResponse(Collections.singletonList(deletedResponse), "Note deleted successfully" , HttpStatus.OK);
    }
}
