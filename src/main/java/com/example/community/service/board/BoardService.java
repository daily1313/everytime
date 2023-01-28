package com.example.community.service.board;

import static java.util.stream.Collectors.toList;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Board.ImageUpdatedResult;
import com.example.community.domain.board.Image;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardCreateResponseDto;
import com.example.community.dto.board.BoardFindAllWithPagingResponseDto;
import com.example.community.dto.board.BoardResponseDto;
import com.example.community.dto.board.BoardSimpleResponseDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.dto.board.PageDto;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.repository.BoardRepository;
import com.example.community.service.file.FileService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileService fileService;

    //게시글 작성
    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto req, Member member){
        List<Image> images = req.getImages().stream()
                .map(i -> new Image(i.getOriginalFilename()))
                .collect(toList());
        Board board = new Board(req.getTitle(), req.getContent(), member, images);
        boardRepository.save(board);
        uploadImages(board.getImages(), req.getImages());
        return new BoardCreateResponseDto(board.getId(), board.getTitle(), board.getContent());
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoards(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Board> boards = boardRepository.findAll(pageRequest);
        List<BoardSimpleResponseDto> allBoards = boards.stream()
                .map(BoardSimpleResponseDto::toDto)
                .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(allBoards, new PageDto(boards));
    }

    //게시글 단건 조회
    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        Member member = board.getMember();
        return BoardResponseDto.toDto(board, member.getUsername());
    }

    //게시글 수정
    @Transactional
    public BoardResponseDto editBoard(Long id, BoardUpdateRequestDto req, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board, member);
        ImageUpdatedResult result = board.update(req);
        uploadImages(result.getAddedImages(),result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return BoardResponseDto.toDto(board, member.getNickname());
    }
    //게시글 삭제
    @Transactional
    public void deleteBoard(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board, member);
        boardRepository.delete(board);
    }
    //게시글 키워드로 검색
    public List<BoardSimpleResponseDto> searchBoard(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByTitleContaining(keyword, pageable);
        List<BoardSimpleResponseDto> allBoards = new ArrayList<>();
        for(Board board : boards) {
            allBoards.add(BoardSimpleResponseDto.toDto(board));
        }
        return allBoards;
    }

    private void validateBoardWriter(Board board, Member member){
        if (!member.equals(board.getMember())){
            throw new MemberNotEqualsException();
        }
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size())
                .forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
