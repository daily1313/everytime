package com.example.community.controller.board;

import com.example.community.domain.member.Member;
import com.example.community.dto.board.BoardCreateRequestDto;
import com.example.community.dto.board.BoardUpdateRequestDto;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.board.BoardService;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class BoardController {
    private final MemberRepository memberRepository;
    private final BoardService boardService;

    @ApiOperation(value = "게시글 작성", notes = "게시글을 작성합니다.")
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createBoard(@Valid @ModelAttribute BoardCreateRequestDto req) {
        Member member = getPrincipal();
        return Response.success(boardService.createBoard(req, member));
    }

    @ApiOperation(value = "게시글 전체 조회", notes = "게시글 전체를 조회합니다.")
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoards(Integer page) {
        return Response.success(boardService.findAllBoards(page));
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글 하나를 조회합니다.")
    @GetMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findBoard(@PathVariable("id") Long id) {
        return Response.success(boardService.findBoard(id));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editBoard(@PathVariable("id") Long id, @Valid @ModelAttribute BoardUpdateRequestDto req) {
        Member member = getPrincipal();
        return Response.success(boardService.editBoard(id, req, member));
    }

    @ApiOperation(value = "게시글 좋아요", notes = "사용자가 좋아요를 눌렀습니다.")
    @PostMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response likeBoard(@PathVariable("id") Long id) {
        Member member = getPrincipal();
        return Response.success(boardService.updateLike(id, member));
    }

    @ApiOperation(value = "게시글 즐겨찾기", notes = "사용자가 즐겨찾기를 눌렀습니다.")
    @PostMapping("/boards/{id}/favorites")
    @ResponseStatus(HttpStatus.OK)
    public Response favoriteBoard(@PathVariable("id") Long id) {
        Member member = getPrincipal();
        return Response.success(boardService.updateFavorite(id, member));
    }

    @ApiOperation(value = "즐겨찾기 게시판을 조회", notes = "즐겨찾기로 등록한 게시판을 조회합니다.")
    @GetMapping("/boards/favorites")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllFavoriteBoards(Integer page) {
        return Response.success(boardService.findAllFavoriteBoards(page, getPrincipal()));
    }

    @ApiOperation(value = "좋아요가 많은 순으로 게시판조회", notes = "게시판을 좋아요순으로 조회합니다.")
    @GetMapping("/boards/likes")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllBoardsWithLikes(Integer page) {
        return Response.success(boardService.findAllBoardsInTheOrderOfHighNumbersOfLikes(page));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/boards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteBoard(@PathVariable("id") Long id) {
        Member member = getPrincipal();
        boardService.deleteBoard(id, member);
        return Response.success("게시글 삭제 성공");
    }

    @ApiOperation(value = "게시글 검색", notes = "게시글을 검색합니다.")
    @GetMapping("/boards/search/{keyword}")
    @ResponseStatus(HttpStatus.OK)
    public Response searchBoard(@PathVariable String keyword,
                                @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(boardService.searchBoard(keyword, pageable));
    }


    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return member;
    }
}
