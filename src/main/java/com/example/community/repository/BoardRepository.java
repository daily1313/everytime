package com.example.community.repository;

import com.example.community.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);
    Page<Board> findAllByTitleContaining(String keyword, Pageable pageable);
}
