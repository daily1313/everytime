package com.example.community.dto.board;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardFindAllWithPagingResponseDto {
    private List<BoardSimpleResponseDto> boards;
    private PageDto pageDto;

    public static BoardFindAllWithPagingResponseDto toDto(List<BoardSimpleResponseDto> boards, PageDto pageDto) {
        return new BoardFindAllWithPagingResponseDto(boards, pageDto);
    }
}
