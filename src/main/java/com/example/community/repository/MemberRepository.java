package com.example.community.repository;


import com.example.community.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsernameAndPassword(String username, String password);
    Optional<Member> findByUsername(String username);
    public boolean existsByUsername(String username);
}
