package com.example.inoue_saki.controller.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "期限を設定してください")
    @FutureOrPresent(message = "無効な日付です")
    private LocalDate limitDate;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    /*
     * content用カスタムバリデーションメソッドの追加
     * NotBlankは全角スペースのみの入力を許してしまう
     */

    @AssertTrue(message = "タスクを入力してください")
    public boolean isContentNotOnlyFullWidthSpace() {
        // null または空文字列の場合、@NotBlankで処理する
        if (content == null || content.isEmpty()) {
            return true;
        }

        // 文字列が全角スペースのみで構成されているか（1文字とは限らない）
        for (char c : content.toCharArray()) {
            // 全角スペース以外の文字を見つけた場合
            if (c != '　') {
                return true;
            }
        }
        // 全ての文字が全角スペースだった場合
        return false;
    }
}
