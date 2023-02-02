package com.example.community.service;

import static com.example.community.factory.MemberFactory.createMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.community.config.jwt.TokenProvider;
import com.example.community.dto.jwt.LoginRequestDto;
import com.example.community.dto.jwt.SignUpRequestDto;
import com.example.community.exception.LoginFailureException;
import com.example.community.repository.MemberRepository;
import com.example.community.repository.RefreshTokenRepository;
import com.example.community.service.auth.AuthService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    AuthService authService;

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void beforeEach() {
        authService = new AuthService(authenticationManagerBuilder, memberRepository, passwordEncoder, tokenProvider, refreshTokenRepository);
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void joinTest() {

        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("kimsb7218","1234","김승범","김승범1");

        //when
        authService.join(signUpRequestDto);

        //then
        verify(passwordEncoder).encode(signUpRequestDto.getPassword());
        verify(memberRepository).save(any());
    }

    @DisplayName("로그인 실패 테스트")
    @Test
    public void loginFailureTest() {

        //given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createMember()));

        assertThatThrownBy(()-> authService.login(new LoginRequestDto("kimsb7218", "1234")))
                .isInstanceOf(LoginFailureException.class);

    }


    @DisplayName("패스워드 검증 테스트")
    @Test
    public void signInExceptionByInvalidPasswordTest() {
        // given
        given(memberRepository.findByUsername(any())).willReturn(Optional.of(createMember()));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> authService.login(new LoginRequestDto("username", "password")))
                .isInstanceOf(LoginFailureException.class);
    }



}
