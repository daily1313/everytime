package com.example.community.dto.message;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDto {

    @ApiModelProperty(value = "메세지 제목", notes = "메세지 제목을 입력해주세요.", required = true, example = "제목")
    @NotBlank(message = "메세지 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "메세지 내용", notes = "메세지 내용을 입력해주세요.", required = true, example = "본문")
    @NotBlank(message = "메세지 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "수신인", notes = "수신자 이름을 입력해주세요.", required = true, example = "김태수")
    @NotBlank(message = "수신인을 입력해주세요.")
    private String receiverUsername;
}
