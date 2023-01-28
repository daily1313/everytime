package com.example.community.dto.board;

import static java.util.stream.Collectors.toList;

import com.example.community.domain.board.Board;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String writer_nickname;
    private String title;
    private String content;
    private List<ImageDto> images;
    private LocalDateTime createdAt;

    public static BoardResponseDto toDto(Board board, String writer_nickname) {
        return new BoardResponseDto(
                board.getId(),
                writer_nickname,
                board.getTitle(),
                board.getContent(),
                board.getImages().stream().map(i -> ImageDto.toDto(i)).collect(toList()),
                board.getCreatedAt()
        );
    }
}
