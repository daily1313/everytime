package com.example.community.service;

import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.community.domain.member.Member;
import com.example.community.domain.member.Role;
import com.example.community.dto.member.MemberRequestDto;
import com.example.community.dto.member.MemberResponseDto;
import com.example.community.repository.MemberRepository;
import com.example.community.service.member.MemberService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.bytebuddy.utility.dispatcher.JavaDispatcher.IsStatic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @DisplayName("전체 회원 조회 테스트")
    @Test
    public void findAllMembersTest() {

        //given
        List<Member> allMembers = new ArrayList<>();

        Member member1 = new Member("sos0212","1234",Role.ROLE_USER,"이재윤","잘생긴뚱이");

        Member member2 = new Member("javajoha","1234",Role.ROLE_USER,"김태수","푸우");

        Member member3 = new Member("daily1313","1234",Role.ROLE_ADMIN,"김승범","범신");

        allMembers.add(member1);
        allMembers.add(member2);
        allMembers.add(member3);

        given(memberRepository.findAll()).willReturn(allMembers);

        //when
        List<MemberResponseDto> result = memberService.findAllMembers();

        //then
        assertThat(result.size()).isEqualTo(allMembers.size());

    }

    @DisplayName("한 명 회원 찾기 테스트")
    @Test
    public void findMemberTest() {

        //given
        Member member = new Member("beom","1234", Role.ROLE_USER,"김승범","beom");
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        MemberResponseDto result = memberService.findMember(1L);

        //then
        assertThat(result.getName()).isEqualTo(member.getName());
    }

    @DisplayName("회원 정보 수정 테스트")
    @Test
    public void editMemberInfoTest() {
        //given
        Member member = new Member("daily1313","1234",Role.ROLE_USER,"김승범","beom");
        MemberRequestDto memberRequestDto = new MemberRequestDto("daily1313","1234","beomm");

        //when
        memberService.editMemberInfo(member,memberRequestDto);

        //then
        assertThat(member.getNickname()).isEqualTo("beomm");
    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    public void deleteMember() {
        //given
        Member member = createMember();

        //when
        memberService.deleteMember(member);

        //then
        verify(memberRepository).delete(member);
    }



}
