package com.example.community.dto.member;

import com.example.community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String name;
    private String password;
    private String nickname;

    public static MemberRequestDto toDto(Member member) {
        return new MemberRequestDto(member.getName(), member.getPassword(), member.getNickname());
    }
}
