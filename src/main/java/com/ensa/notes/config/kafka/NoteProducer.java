package com.ensa.notes.config.kafka;

import com.ensa.notes.dtos.NoteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteProducer {

    private final KafkaTemplate<String, NoteRequest> kafkaTemplate;

    public void send(NoteRequest noteRequest) {
        log.info(" [PRODUCER] Send to kafka : {}", noteRequest);

        kafkaTemplate.send("note-topic", noteRequest.getTitle(), noteRequest );
    }
}
