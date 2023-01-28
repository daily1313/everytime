package com.example.community.dto.board;

import com.example.community.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleResponseDto {
    private Long id;
    private String title;
    private String nickname;


    public static BoardSimpleResponseDto toDto(Board board) {
        return new BoardSimpleResponseDto(board.getId(), board.getTitle(), board.getMember().getNickname());
    }
}
