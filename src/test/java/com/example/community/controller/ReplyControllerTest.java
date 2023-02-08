package com.example.community.controller;

import static com.example.community.factory.MemberFactory.createMember;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.community.controller.reply.ReplyController;
import com.example.community.domain.member.Member;
import com.example.community.dto.reply.ReplyCreateRequestDto;
import com.example.community.dto.reply.ReplyEditRequestDto;
import com.example.community.dto.reply.ReplyReadNumber;
import com.example.community.repository.MemberRepository;
import com.example.community.service.reply.ReplyService;
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

@ExtendWith(MockitoExtension.class)
public class ReplyControllerTest {

    @InjectMocks
    ReplyController replyController;

    @Mock
    ReplyService replyService;

    @Mock
    MemberRepository memberRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).build();
    }

    @DisplayName("댓글 조회 테스트")
    @Test
    public void findAllRepliesTest() throws Exception {

        //given
        ReplyReadNumber req = new ReplyReadNumber(1L);

        //when
        mockMvc.perform(
                get("/api/replies")
                        .param("boardId",String.valueOf(req.getBoardId()))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        //then
        verify(replyService).findAllReplies(req);


    }

    @DisplayName("댓글 작성 테스트")
    @Test
    public void createReply() throws Exception {

        ReplyCreateRequestDto req = new ReplyCreateRequestDto(1L, "comment");

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when

        mockMvc.perform(
                        post("/api/replies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(req)))
                .andExpect(status().isCreated());

        //then
        verify(replyService).createReply(req, member);

    }

    @DisplayName("댓글 수정 테스트")
    @Test
    public void editReply() throws Exception {
        //given
        Long replyId = 1L;
        ReplyEditRequestDto req = new ReplyEditRequestDto("comment2");

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when

        mockMvc.perform(
                put("/api/replies/{replyId}",replyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        ).andExpect(status().isOk());

        //then
        verify(replyService).editReply(req, member, replyId);

    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    public void deleteReply() throws Exception {
        //given
        Long replyId = 1L;

        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                        delete("/api/replies/{replyId}", replyId))
                .andExpect(status().isOk());

        //then
        verify(replyService).deleteReply(replyId, member);

    }


}
