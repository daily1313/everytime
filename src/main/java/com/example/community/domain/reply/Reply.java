package com.example.community.domain.reply;


import com.example.community.domain.board.Board;
import com.example.community.domain.common.EntityDate;
import com.example.community.domain.member.Member;
import com.example.community.dto.reply.ReplyEditRequestDto;
import com.example.community.dto.reply.ReplyResponseDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// 생성자 없는 매개변수 접근 레벨 PROTECTED
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Reply extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    public Reply(String comment, Member member, Board board) {
        this.comment = comment;
        this.member = member;
        this.board = board;
    }

    public void editReply(ReplyEditRequestDto req) {
        this.comment = req.getComment();
    }
}
