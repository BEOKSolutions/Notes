package com.ensa.notes.config.kafka;

import com.ensa.notes.dtos.NoteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NoteConsumer {

    @KafkaListener(topics = "note-topic", groupId = "note-group")
    public void consumerNote(
            @Payload NoteRequest noteRequest,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
            ) {

        log.info("Received partition {}, offset {}", partition, offset);


    }
}
