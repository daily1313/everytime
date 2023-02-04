package com.example.community.controller;

import static com.example.community.factory.MemberFactory.createMember;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.community.controller.message.MessageController;
import com.example.community.domain.member.Member;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.repository.MemberRepository;
import com.example.community.service.message.MessageService;
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
public class MessageControllerTest {
    @InjectMocks
    MessageController messageController;

    @Mock
    MessageService messageService;

    @Mock
    MemberRepository memberRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @DisplayName("쪽지 작성 테스트")
    @Test
    public void CreateMessageTest() throws Exception {
        //given
        Member sender = createMember();
        MessageCreateRequestDto req = new MessageCreateRequestDto("title","content","receiver");
        Authentication authentication = new UsernamePasswordAuthenticationToken(sender.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(sender));

        //when
        mockMvc.perform(post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))

                .andExpect(status().isCreated());

        verify(messageService).createMessage(refEq(sender), refEq(req));
    }

    @DisplayName("전체 수신 쪽지 조회 테스트")
    @Test
    public void findAllReceivedMessagesTest() throws Exception {
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/receiver")
        ).andExpect(status().isOk());

        //then
        verify(messageService).findAllReceivedMessages(member);
    }

    @DisplayName("단건 수신 쪽지 조회 테스트")
    @Test
    public void findReceivedMessageTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/receiver/{id}", id)
        ).andExpect(status().isOk());

        //then
        verify(messageService).findReceivedMessage(id, member);
    }

    @DisplayName("전체 발신 쪽지 조회 테스트")
    @Test
    public void findAllSentMessagesTest() throws Exception {
        //given
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/sender")
        ).andExpect(status().isOk());

        //then
        verify(messageService).findAllSentMessages(member);
    }

    @DisplayName("단건 발신 쪽지 조회 테스트")
    @Test
    public void findSentMessageTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                get("/api/messages/sender/{id}",id)
        ).andExpect(status().isOk());

        //then
        verify(messageService).findSentMessage(id, member);
    }

    @DisplayName("수신 메세지 삭제 테스트")
    @Test
    public void deleteMessageByReceiverTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/messages/receiver/{id}",id)
        ).andExpect(status().isOk());

        //then
        verify(messageService).deleteMessageByReceiver(id, member);
    }

    @DisplayName("발신 메세지 삭제 테스트")
    @Test
    void deleteMessageBySenderTest() throws Exception {
        //given
        Long id = 1L;
        Member member = createMember();
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getId(), "",
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(memberRepository.findByUsername(authentication.getName())).willReturn(Optional.of(member));

        //when
        mockMvc.perform(
                delete("/api/messages/sender/{id}",id))
                .andExpect(status().isOk());

        //then
        verify(messageService).deleteMessageBySender(id, member);
    }


}
