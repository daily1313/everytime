package com.example.community.service;

import static com.example.community.factory.BoardFactory.createBoard;
import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Image;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardCreateResponseDto;
import com.example.community.dto.board.BoardFindAllWithPagingResponseDto;
import com.example.community.dto.board.BoardResponseDto;
import com.example.community.dto.board.BoardSimpleResponseDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.FavoriteRepository;
import com.example.community.repository.LikesRepository;
import com.example.community.service.board.BoardService;
import com.example.community.service.file.FileService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    LikesRepository likesRepository;

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    FileService fileService;

    @DisplayName("게시판 작성 테스트")
    @Test
    public void createBoardTest() {
        //given
        BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto("title","content",
                List.of(new MockMultipartFile("test","test.png", MediaType.IMAGE_PNG_VALUE,"test".getBytes())));

        Member member = createMember();
        //when
        boardService.createBoard(boardCreateRequestDto,member);

        //then
        verify(boardRepository).save(any());
    }

    @DisplayName("게시판 전체 조회 테스트")
    @Test
    public void findAllBoardsTest() {
        //given
        Integer page = 2;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        List<Board> allboards = new ArrayList<>();
        Board board1 = createBoard();
        Board board2 = createBoard();
        allboards.add(board1);
        allboards.add(board2);

        Page<Board> boardsWithPaging = new PageImpl<>(allboards);
        given(boardRepository.findAll(pageRequest)).willReturn(boardsWithPaging);

        //when
        BoardFindAllWithPagingResponseDto result = boardService.findAllBoards(page);

        //then
        assertThat(result.getBoards().size()).isEqualTo(2);

    }
    @DisplayName("게시글 단건 조회 테스트")
    @Test
    public void findBoardTest() {
        //given
        Long id = 1L;
        Board board = createBoard();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        BoardResponseDto result = boardService.findBoard(id);

        //then
        assertThat(result.getTitle()).isEqualTo("title");

    }

    @DisplayName("게시글 수정 테스트")
    @Test
    public void editBoardTest() {
        //given
        Long id = 1L;

        Member member = createMember();
        Board board = new Board("title2","content2",member,List.of(new Image("test2.png")));
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "c".getBytes());
        BoardUpdateRequestDto req = new BoardUpdateRequestDto("title3", "content2", List.of(cFile), List.of());
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        boardService.editBoard(id, req, member);

        //then
        assertThat(board.getTitle()).isEqualTo("title3");

    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    public void deleteBoardTest() {

        //given
        Long id = 1L;
        Board board = createBoard();
        Member member = board.getMember();
        given(boardRepository.findById(id)).willReturn(Optional.of(board));

        //when
        boardService.deleteBoard(id, member);

        //then
        verify(boardRepository).delete(any());

    }

}
