package com.example.community.service;

import static com.example.community.factory.MemberFactory.createMember;
import static com.example.community.factory.MemberFactory.createReceivedMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.community.domain.member.Member;
import com.example.community.domain.message.Message;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.dto.message.MessageResponseDto;
import com.example.community.repository.MemberRepository;
import com.example.community.repository.MessageRepository;
import com.example.community.service.message.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MemberRepository memberRepository;

    @DisplayName("쪽지 작성 테스트")
    @Test
    public void createMessageTest() {
        //given
        Member sender = createMember();
        Member receiver = createReceivedMember();
        MessageCreateRequestDto req = new MessageCreateRequestDto("title","content", receiver.getUsername());

        given(memberRepository.findByUsername(req.getReceiverUsername())).willReturn(Optional.of(receiver));
        Message message = Message.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .sender(sender)
                .receiver(receiver)
                .build();
        //when
        MessageResponseDto result = messageService.createMessage(sender, req);

        //then
        assertThat(result.getTitle()).isEqualTo("title");
    }

    @DisplayName("전체 수신 쪽지 조회 테스트")
    @Test
    public void findAllReceivedMessagesTest() {
        //given
        Member sender = createMember();
        Member receiver = createReceivedMember();
        List<Message> messages = new ArrayList<>();
        Message m1 = new Message("11","11",sender,receiver);
        Message m2 = new Message("11","11",sender,receiver);
        messages.add(m1);
        messages.add(m2);
        given(messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(receiver)).willReturn(messages);

        //when
        List<MessageResponseDto> result = messageService.findAllReceivedMessages(receiver);

        //then
        assertThat(result.size()).isEqualTo(2);

    }

    @DisplayName("단건 수신 쪽지 조회 테스트")
    @Test
    public void findReceivedMessage() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createReceivedMember();
        Message message = new Message("title","content",sender,receiver);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        //when
        MessageResponseDto result = messageService.findReceivedMessage(id, receiver);

        //then
        assertThat(result.getTitle()).isEqualTo("title");

    }

    @DisplayName("전체 발신 쪽지 조회 테스트")
    @Test
    public void findAllSentMessagesTest() {
        //given
        Member sender = createMember();
        Member receiver = createReceivedMember();
        List<Message> messages = new ArrayList<>();
        Message m1 = new Message("11","11",sender,receiver);
        Message m2 = new Message("11","11",sender,receiver);
        messages.add(m1);
        messages.add(m2);
        given(messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(sender)).willReturn(messages);

        //when
        List<MessageResponseDto> result = messageService.findAllSentMessages(sender);

        //then
        assertThat(result.size()).isEqualTo(2);

    }

    @DisplayName("단건 발신 쪽지 조회 테스트")
    @Test
    public void findSentMessage() {
        //given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createReceivedMember();
        Message message = new Message("title","content",sender,receiver);
        given(messageRepository.findById(anyLong())).willReturn(Optional.of(message));

        //when
        MessageResponseDto result = messageService.findSentMessage(id, sender);

        //then
        assertThat(result.getTitle()).isEqualTo("title");

    }

    @DisplayName("수신 메세지 삭제 테스트")
    @Test
    public void deleteMessageByReceiver() {
        // given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createReceivedMember();
        Message message = new Message("title","content",sender,receiver);
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
       messageService.deleteMessageByReceiver(id, receiver);

       //then
        assertThat(message.isDeletedByReceiver()).isTrue();

    }

    @DisplayName("발신 메세지 삭제 테스트")
    @Test
    public void deleteMessageBySender() {
        // given
        Long id = 1L;
        Member sender = createMember();
        Member receiver = createReceivedMember();
        Message message = new Message("title","content",sender,receiver);
        given(messageRepository.findById(id)).willReturn(Optional.of(message));

        // when
        messageService.deleteMessageBySender(id, sender);

        //then
        assertThat(message.isDeletedBySender()).isTrue();

    }

}
