package com.example.community.service.member;


import com.example.community.controller.member.MemberController;
import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberRequestDto;
import com.example.community.dto.member.MemberResponseDto;
import com.example.community.exception.UserNotFoundException;
import com.example.community.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> allMembers = members.stream()
                .map(MemberResponseDto::toDto)
                .collect(Collectors.toList());

        return allMembers;
    }

    // 회원 단건 조회
    @Transactional(readOnly = true)
    public MemberResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return MemberResponseDto.toDto(member);
    }

    // 회원 단건 조회(이름으로 조회)
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByUsername(String username) {
        Member member2 = memberRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return MemberResponseDto.toDto(member2);
    }

    // 회원 수정
    @Transactional
    public Member editMemberInfo(Member member, MemberRequestDto memberRequestDto) {
        member.editMember(memberRequestDto);
        memberRepository.save(member);
        return member;
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

}
