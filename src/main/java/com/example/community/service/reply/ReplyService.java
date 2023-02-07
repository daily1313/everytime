package com.example.community.service.reply;

import com.example.community.domain.board.Board;
import com.example.community.domain.member.Member;
import com.example.community.domain.reply.Reply;
import com.example.community.dto.reply.ReplyCreateRequestDto;
import com.example.community.dto.reply.ReplyEditRequestDto;
import com.example.community.dto.reply.ReplyReadNumber;
import com.example.community.dto.reply.ReplyResponseDto;
import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.MemberNotEqualsException;
import com.example.community.exception.ReplyNotFoundException;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.ReplyRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<ReplyResponseDto> findAllReplies(ReplyReadNumber readNumber) {
        List<Reply> replies = replyRepository.findAllByBoardId(readNumber.getBoardId());
        List<ReplyResponseDto> allReplies = new ArrayList<>();
        for(Reply reply : replies) {
            allReplies.add(ReplyResponseDto.toDto(reply));
        }
        return allReplies;
    }

    @Transactional
    public ReplyResponseDto createReply(ReplyCreateRequestDto req, Member member) {
        Board board = boardRepository.findById(req.getBoardId()).orElseThrow(BoardNotFoundException::new);
        Reply reply = new Reply(req.getComment(), member, board);
        replyRepository.save(reply);
        return ReplyResponseDto.toDto(reply);
    }

    @Transactional
    public ReplyResponseDto editReply(ReplyEditRequestDto req, Member member, Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(ReplyNotFoundException::new);
        validateReply(reply, member);
        reply.editReply(req);
        return ReplyResponseDto.toDto(reply);
    }

    @Transactional
    public void deleteReply(Long id, Member member) {
        Reply reply = replyRepository.findById(id).orElseThrow(ReplyNotFoundException::new);
        validateReply(reply, member);
        replyRepository.delete(reply);
    }

    private void validateReply(Reply reply, Member member) {
        if(!reply.getMember().equals(member)) {
            throw new MemberNotEqualsException();
        }
    }
}
