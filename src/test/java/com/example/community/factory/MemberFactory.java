package com.example.community.factory;

import com.example.community.domain.member.Member;
import com.example.community.domain.member.Role;

public class MemberFactory {

    public static Member createMember() {
        Member member = Member.builder()
                .username("kimsb7218")
                .name("김승범")
                .password("1234")
                .role(Role.ROLE_USER)
                .nickname("nickname")
                .build();
        return member;
    }

    public static Member createReceivedMember() {
        Member member = Member.builder()
                .username("fdsfds")
                .name("fdsfsdf")
                .password("12346")
                .role(Role.ROLE_USER)
                .nickname("nickname")
                .build();
        return member;
    }
}
