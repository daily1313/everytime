package com.example.community.domain.member;


import com.example.community.domain.common.EntityDate;
import com.example.community.dto.member.MemberRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Member extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;


    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;



    @Builder
    public Member(String username, String password, Role role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
    }

    public void editMember(MemberRequestDto memberRequestDto) {
        name = memberRequestDto.getName();
        password = memberRequestDto.getPassword();
        nickname = memberRequestDto.getNickname();
    }



}
