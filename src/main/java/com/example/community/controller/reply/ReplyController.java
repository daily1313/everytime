package com.example.community.controller.reply;


import com.example.community.domain.member.Member;
import com.example.community.dto.reply.ReplyCreateRequestDto;
import com.example.community.dto.reply.ReplyEditRequestDto;
import com.example.community.dto.reply.ReplyReadNumber;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.reply.ReplyService;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "전체 댓글 조회", notes = "모든 댓글을 조회합니다.")
    @GetMapping("/replys")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllReplies(ReplyReadNumber readNumber) {
        return Response.success(replyService.findAllReplies(readNumber));
    }

    @ApiOperation(value = "댓글 작성", notes = "댓글을 작성합니다.")
    @PostMapping("/replys")
    @ResponseStatus(HttpStatus.CREATED)
    public Response createReply(@Valid @RequestBody ReplyCreateRequestDto req) {
        Member member = getPrincipal();
        return Response.success(replyService.createReply(req, member));
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정합니다.")
    @PostMapping("/replys/{replyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response editReply(@Valid @RequestBody ReplyEditRequestDto req, @PathVariable("replyId") Long replyId) {
        Member member = getPrincipal();
        return Response.success(replyService.editReply(req, member, replyId));
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/replys/{replyId}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteReply(@PathVariable("replyId") Long replyId) {
        Member member = getPrincipal();
        replyService.deleteReply(replyId, member);
        return Response.success("댓글을 삭제하였습니다.");
    }

    private Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return member;
    }
}
