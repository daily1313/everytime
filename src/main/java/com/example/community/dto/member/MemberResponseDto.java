package com.example.community.dto.member;

import com.example.community.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String name;
    private String password;
    private String nickname;

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(member.getName(), member.getPassword(), member.getNickname());
    }
}
