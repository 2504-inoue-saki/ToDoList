package com.example.inoue_saki.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter

public class TasksForm {

    private int id;

    @NotBlank(message = "タスクを入力してください")
    @Length(max = 140, message = "タスクは140文字以内で入力してください")
    private String content;

    private short status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate limitDate;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
