package com.example.community.controller.member;


import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberRequestDto;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.MemberRepository;
import com.example.community.response.Response;
import com.example.community.service.member.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public Response findAllMembers() {
        return Response.success(memberService.findAllMembers());
    }

    @ApiOperation(value = "단건 회원 조회", notes = "회원 한 명을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members/{id}")
    public Response findMember(@PathVariable("id") Long id) {
        return Response.success(memberService.findMember(id));
    }

    @ApiOperation(value = "단건 회원 조회(이름으로 조회)", notes = "회원 한 명을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/member/{username}")
    public Response findMemberByUsername(@PathVariable("username") String username) {
        return Response.success(memberService.findMemberByUsername(username));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/members")
    public Response editMemberInfo(@RequestBody MemberRequestDto memberRequestDto) {
        Member memberInfo = getPrincipal();
        return Response.success(memberService.editMemberInfo(memberInfo, memberRequestDto));
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/members")
    public Response deleteMember() {
        Member memberInfo = getPrincipal();
        memberService.deleteMember(memberInfo);
        return Response.success("회원 탈퇴 성공");
    }


    // 유저 정보르 가져오는 getPrincipal 함수
    public Member getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return member;
    }


}
