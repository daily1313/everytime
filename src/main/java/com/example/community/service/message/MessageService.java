package com.example.community.service.message;

import com.example.community.domain.member.Member;
import com.example.community.domain.message.Message;
import com.example.community.dto.message.MessageCreateRequestDto;
import com.example.community.dto.message.MessageResponseDto;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.exception.MessageNotFoundException;
import com.example.community.repository.MemberRepository;
import com.example.community.repository.MessageRepository;
import java.util.ArrayList;
import java.util.List;
import javax.el.MethodNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    // 쪽지 작성
    //
    @Transactional
    public MessageResponseDto createMessage(Member sender, MessageCreateRequestDto req) {
        Member receiver = memberRepository.findByUsername(req.getReceiverUsername()).orElseThrow(
                MethodNotFoundException::new);
        Message message = Message.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .sender(sender)
                .receiver(receiver)
                .build();
        return MessageResponseDto.toDto(message);
    }

    //전체 수신 쪽지 조회
    @Transactional(readOnly = true)
    public List<MessageResponseDto> findAllReceivedMessages(Member member) {
        List<Message> findMessages = messageRepository.findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(member);
        List<MessageResponseDto> allMessages = new ArrayList<>();

        for (Message message : findMessages) {
            if (!message.isDeletedByReceiver()) {
                allMessages.add(MessageResponseDto.toDto(message));
            }
        }
        return allMessages;
    }

    //단건 수신 쪽지 조회
    @Transactional(readOnly = true)
    public MessageResponseDto findReceivedMessage(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        if (!message.getReceiver().equals(member)) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedByReceiver()) {
            throw new MessageNotFoundException();
        }
        return MessageResponseDto.toDto(message);
    }

    //전체 발신 쪽지 조회
    @Transactional(readOnly = true)
    public List<MessageResponseDto> findAllSentMessages(Member member) {
        List<Message> findMessages = messageRepository.findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(member);
        List<MessageResponseDto> allMessages = new ArrayList<>();

        for (Message message : findMessages) {
            if (!message.isDeletedBySender()) {
                allMessages.add(MessageResponseDto.toDto(message));
            }
        }

        return allMessages;
    }

    //단건 발신 쪽지 조회
    @Transactional(readOnly = true)
    public MessageResponseDto findSentMessage(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        if (!message.getSender().equals(member)) {
            throw new MemberNotEqualsException();
        }
        if (message.isDeletedBySender()) {
            throw new MessageNotFoundException();
        }
        return MessageResponseDto.toDto(message);
    }

    // 메세지 삭제(수신자에 의해 삭제)
    @Transactional(readOnly = true)
    public void deleteMessageByReceiver(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        if (!message.getReceiver().equals(member)) {
            throw new MemberNotEqualsException();
        }
        message.deleteByReceiver();

        if (message.isDeletedByReceiver()) {
            messageRepository.delete(message);
        }

    }

    // 메세지 삭제(발신자에 의해 삭제)
    @Transactional(readOnly = true)
    public void deleteMessageBySender(Long id, Member member) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        if (!message.getSender().equals(member)) {
            throw new MemberNotEqualsException();
        }
        message.deleteBySender();

        if (message.isDeletedBySender()) {
            messageRepository.delete(message);
        }

    }


}
