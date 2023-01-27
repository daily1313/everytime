package com.example.community.controller;

import static com.example.community.factory.MemberFactory.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.community.controller.auth.AuthController;
import com.example.community.controller.member.MemberController;
import com.example.community.domain.member.Member;
import com.example.community.dto.member.MemberRequestDto;
import com.example.community.repository.MemberRepository;
import com.example.community.service.auth.AuthService;
import com.example.community.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// Mockito에서 제공하는 목객체를 사용하기 하기위해 위와같은 어노테이션을 테스트클래스에 달아준다.
@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    // @InjectMocks가 붙은 객체에 @Mock를 주입
    @InjectMocks
    MemberController memberController;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberService memberService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @DisplayName("전체 회원 조회 테스트")
    @Test
    public void findAllMembersTest() throws Exception {

        //when
        mockMvc.perform(
                get("/api/members"))
                .andExpect(status().isOk());
        //then
        verify(memberService).findAllMembers();

    }

    @DisplayName("회원 단건 조회 테스트")
    @Test
    public void findMemberTest() throws Exception {

        //given
        Long id = 1L;

        //when
        mockMvc.perform(
                get("/api/members/{id}",id))
                .andExpect(status().isOk());

        //then
        verify(memberService).findMember(id);
    }

    @DisplayName("회원 수정 테스트")
    @Test
    public void editUserInfoTest() throws Exception {

        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("김승범","password","nickname");
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(),"", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                put("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andExpect(status().isOk());

        //then
        verify(memberService).editMemberInfo(member, memberRequestDto);
        assertThat(memberRequestDto.getName()).isEqualTo("김승범");
    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    public void deleteMemberTest() throws Exception {

        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(),"",Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/members"))
                .andExpect(status().isOk());

        //then
        verify(memberService).deleteMember(member);
    }



}
