package com.example.community.repository;
//
import com.example.community.domain.member.Member;
import com.example.community.domain.message.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(Member member);
    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(Member member);

}
