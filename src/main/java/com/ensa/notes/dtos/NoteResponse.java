package com.ensa.notes.dtos;

import com.ensa.notes.enums.Level;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class NoteResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID code;
    private String title;
    private String content;
    private Level level;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
