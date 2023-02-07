package com.example.community.repository;

import com.example.community.domain.reply.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBoardId(Long boardId);
}
