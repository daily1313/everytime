package com.example.community.controller.message;


import com.example.community.domain.member.Member;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.member.MemberService;
import com.example.community.service.message.MessageService;
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
//
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "쪽지 작성", notes = "쪽지를 보냈습니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages")
    public Response createMessage(@Valid @RequestBody MessageCreateRequestDto req) {
        Member sender = getPrincipal();
        return Response.success(messageService.createMessage(sender, req));
    }

    @ApiOperation(value = "받은 쪽지 확인", notes = "받은 쪽지함을 확인하였습니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/receiver")
    public Response findAllReceivedMessages() {
        Member receiver = getPrincipal();
        return Response.success(messageService.findAllReceivedMessages(receiver));
    }

    @ApiOperation(value = "받은 쪽지 1개 확인", notes = "받은 쪽지를 한 개 확인하였습니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/receiver/{id}")
    public Response findReceivedMessage(@PathVariable("id") Long id) {
        Member receiver = getPrincipal();
        return Response.success(messageService.findReceivedMessage(id, receiver));
    }

    @ApiOperation(value = "보낸 쪽지 확인", notes = "보낸 쪽지함을 확인하였습니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sender")
    public Response findAllSentMessages() {
        Member sender = getPrincipal();
        return Response.success(messageService.findAllSentMessages(sender));
    }

    @ApiOperation(value = "보낸 쪽지 1개 확인", notes = "보낸 쪽지을 1개 확인하였습니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/messages/sender/{id}")
    public Response findSentMessage(@PathVariable("id") Long id) {
        Member sender = getPrincipal();
        return Response.success(messageService.findSentMessage(id, sender));
    }

    @ApiOperation(value = "받은 쪽지 삭제", notes = "받은 쪽지 1개를 삭제하였습니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/receiver/{id}")
    public Response deleteMessageByReceiver(@PathVariable("id") Long id) {
        Member receiver = getPrincipal();
        messageService.deleteMessageByReceiver(id, receiver);
        return Response.success("받은 메세지 삭제 성공");
    }

    @ApiOperation(value = "보낸 쪽지 삭제", notes = "보낸 쪽지 1개를 삭제하였습니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/messages/sender/{id}")
    public Response deleteMessageBySender(@PathVariable("id") Long id) {
        Member sender = getPrincipal();
        messageService.deleteMessageBySender(id, sender);
        return Response.success("보낸 메세지 삭제 성공");
    }


    public Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return member;
    }
}
