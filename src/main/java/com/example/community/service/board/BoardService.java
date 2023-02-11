package com.example.community.service.board;

import static java.util.stream.Collectors.toList;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Board.ImageUpdatedResult;
import com.example.community.domain.board.Favorite;
import com.example.community.domain.board.Image;
import com.example.community.domain.board.Likes;
import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardCreateResponseDto;
import com.example.community.dto.board.BoardFindAllWithPagingResponseDto;
import com.example.community.dto.board.BoardResponseDto;
import com.example.community.dto.board.BoardSimpleResponseDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.dto.board.PageDto;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.FavoriteNotFoundException;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.FavoriteRepository;
import com.example.community.repository.LikesRepository;
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
    private final FavoriteRepository favoriteRepository;
    private final LikesRepository likesRepository;

    //게시글 작성
    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto req, Member member) {
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
        board.increaseViewCount();
        return BoardResponseDto.toDto(board, member.getUsername());
    }

    //게시글 수정
    @Transactional
    public BoardResponseDto editBoard(Long id, BoardUpdateRequestDto req, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        validateBoardWriter(board, member);
        ImageUpdatedResult result = board.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
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
        for (Board board : boards) {
            allBoards.add(BoardSimpleResponseDto.toDto(board));
        }
        return allBoards;
    }

    @Transactional
    public String updateLike(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (!hasLike(board, member)) {
            board.increaseLikeCount();
            return addLike(board, member);
        }
        board.decreaseLikeCount();
        return cancelLike(board, member);
    }

    @Transactional
    public String updateFavorite(Long id, Member member) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        if (!hasFavorite(board, member)) {
            return addFavorite(board, member);
        }
        return cancelFavorite(board, member);
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllBoardsInTheOrderOfHighNumbersOfLikes(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("likeCount").descending());
        Page<Board> boards = boardRepository.findAll(pageRequest);
        List<BoardSimpleResponseDto> allBoards = boards.stream().map(BoardSimpleResponseDto::toDto)
                .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(allBoards, new PageDto(boards));
    }

    @Transactional(readOnly = true)
    public BoardFindAllWithPagingResponseDto findAllFavoriteBoards(Integer page, Member member) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Favorite> favorites = favoriteRepository.findAllByMember(member, pageRequest);
        List<BoardSimpleResponseDto> boardsWithDto = favorites.stream().map(Favorite::getBoard)
                .map(BoardSimpleResponseDto::toDto)
                .collect(toList());
        return BoardFindAllWithPagingResponseDto.toDto(boardsWithDto, new PageDto(favorites));
    }


    // 좋아요를 누를 수 있는 상태인지 check
    private boolean hasLike(Board board, Member member) {
        return likesRepository.findByBoardAndMember(board, member).isPresent();
    }

    // 좋아요 누르기
    private String addLike(Board board, Member member) {
        Likes like = new Likes(board, member);
        likesRepository.save(like);
        return "좋아요를 눌렀습니다.";
    }

    // 좋아요 취소
    private String cancelLike(Board board, Member member) {
        Likes like = likesRepository.findByBoardAndMember(board, member).orElseThrow(BoardNotFoundException::new);
        likesRepository.delete(like);
        return "좋아요를 취소하였습니다.";
    }

    // 즐겨찾기를 해놓은 상태인지 check
    private boolean hasFavorite(Board board, Member member) {
        return favoriteRepository.findByBoardAndMember(board, member).isPresent();
    }

    // 즐겨찾기 추가
    private String addFavorite(Board board, Member member) {
        Favorite favorite = new Favorite(board, member);
        favoriteRepository.save(favorite);
        return "게시판을 즐겨찾기에 추가합니다.";
    }

    // 즐겨찾기 취소
    private String cancelFavorite(Board board, Member member) {
        Favorite favorite = favoriteRepository.findByBoardAndMember(board, member)
                .orElseThrow(FavoriteNotFoundException::new);
        favoriteRepository.delete(favorite);
        return "게시판을 즐겨찾기에서 취소하였습니다.";
    }


    private void validateBoardWriter(Board board, Member member) {
        if (!member.equals(board.getMember())) {
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
