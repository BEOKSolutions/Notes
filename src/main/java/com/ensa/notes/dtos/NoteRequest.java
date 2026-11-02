package com.ensa.notes.dtos;

import com.ensa.notes.enums.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    @NotBlank(message = "The title can not be empty or null")
    @Size(min = 3, max = 50, message = "The title must between 3 and 50 characters")
    private String title;

    @NotBlank(message = "The content can not be empty")
    private String content;

    @NotNull(message = "Level must be one off this list: (LOW, MEDIUM or HIGH)")
    private Level level;
}
