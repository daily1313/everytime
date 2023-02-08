package com.example.community.service;

import static com.example.community.factory.BoardFactory.createBoard;
import static com.example.community.factory.MemberFactory.createMember;
import static com.example.community.factory.ReplyFactory.createReply;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.community.domain.board.Board;
import com.example.community.domain.member.Member;
import com.example.community.domain.reply.Reply;
import com.example.community.dto.reply.ReplyCreateRequestDto;
import com.example.community.dto.reply.ReplyEditRequestDto;
import com.example.community.dto.reply.ReplyReadNumber;
import com.example.community.dto.reply.ReplyResponseDto;
import com.example.community.repository.BoardRepository;
import com.example.community.repository.MemberRepository;
import com.example.community.repository.ReplyRepository;
import com.example.community.service.reply.ReplyService;
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
public class ReplyServiceTest {

    @InjectMocks
    ReplyService replyService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    ReplyRepository replyRepository;

    @DisplayName("댓글 조회 테스트")
    @Test
    public void findAllReplies() {
        //given
        List<Reply> allReplies = new ArrayList<>();
        Reply reply = createReply();
        allReplies.add(reply);
        ReplyReadNumber replyReadNumber = new ReplyReadNumber(1L);
        given(replyRepository.findAllByBoardId(replyReadNumber.getBoardId())).willReturn(allReplies);

        //when
        List<ReplyResponseDto> result = replyService.findAllReplies(replyReadNumber);

        //then
        assertThat(result.size()).isEqualTo(1);

    }

    @DisplayName("댓글 작성 테스트")
    @Test
    public void createReplyTest() {
        //given
        Board board = createBoard();
        board.setId(1L);
        ReplyCreateRequestDto req = new ReplyCreateRequestDto(board.getId(), "comment");
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        //when
        ReplyResponseDto result = replyService.createReply(req, createMember());

        //then
        assertThat(result.getComment()).isEqualTo(req.getComment());
    }

    @DisplayName("댓글 수정 테스트")
    @Test
    public void editReplyTest() {
        //given
        Member member = createMember();
        Reply reply = Reply.builder()
                .comment("comment")
                .member(member)
                .board(createBoard())
                .build();
        ReplyEditRequestDto req = new ReplyEditRequestDto("comment2");
        given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));

        //when
        replyService.editReply(req, member, 1L);

        //then
        assertThat(reply.getComment()).isEqualTo("comment2");
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    public void deleteReplyTest() {

        //given
        Member member = createMember();
        Reply reply = createReply(member);
        given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));

        //when

        replyService.deleteReply(anyLong(), member);

        //then
        verify(replyRepository).delete(any());

    }

}
