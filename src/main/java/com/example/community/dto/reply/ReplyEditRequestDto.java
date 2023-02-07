package com.example.community.dto.reply;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyEditRequestDto {

    @NotBlank(message = "수정할 댓글을 입력해주세요.")
    private String comment;
}
