package com.example.community.dto.reply;

import com.example.community.domain.common.EntityDate;
import com.example.community.domain.reply.Reply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyResponseDto extends EntityDate {
    private Long id;
    private String comment;
    private String username;

    public static ReplyResponseDto toDto(Reply reply) {
        return new ReplyResponseDto(
                reply.getId(),
                reply.getComment(),
                reply.getMember().getUsername()
        );
    }
}
