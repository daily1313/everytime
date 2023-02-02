package com.example.community.dto.message;

import com.example.community.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private String title;
    private String content;
    private String senderUsername;
    private String receiverUsername;

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getTitle(),
                message.getContent(),
                message.getSender().getUsername(),
                message.getReceiver().getUsername()
        );
    }
}
