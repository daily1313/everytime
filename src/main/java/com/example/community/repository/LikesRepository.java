package com.example.community.repository;

import com.example.community.domain.board.Board;
import com.example.community.domain.board.Likes;
import com.example.community.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByBoardAndMember(Board board, Member member);
}
