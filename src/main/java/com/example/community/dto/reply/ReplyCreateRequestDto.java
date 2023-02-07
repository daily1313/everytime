package com.example.community.dto.reply;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCreateRequestDto {

    @NotNull(message = "게시글 번호를 입력해주세요")
    private Long boardId;

    @NotBlank(message = "댓글을 입력해주세요.")
    private String comment;
}
