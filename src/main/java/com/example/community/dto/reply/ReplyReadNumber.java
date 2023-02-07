package com.example.community.dto.reply;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyReadNumber {
    @NotNull(message = "게시글 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 게시글 번호를 입력해주세요.")
    private Long boardId;
}
