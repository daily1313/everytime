package com.example.community.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.community.controller.auth.AuthController;
import com.example.community.dto.jwt.LoginRequestDto;
import com.example.community.dto.jwt.SignUpRequestDto;
import com.example.community.dto.jwt.TokenRequestDto;
import com.example.community.dto.jwt.TokenResponseDto;
import com.example.community.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    AuthService authService;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @DisplayName("회원가입 테스트")
    @Test
    public void joinTest() throws Exception {

        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("kimsb7218", "1234", "김승범");

        //when
        mockMvc.perform(
                post("/api/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isCreated());

        //then
        verify(authService).join(signUpRequestDto);
    }

    @DisplayName("로그인 테스트")
    @Test
    public void loginTest() throws Exception {

        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("kimsb7218","1234");
        given(authService.login(loginRequestDto)).willReturn(new TokenResponseDto("access","refresh"));

        //when
        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

        //then
        verify(authService).login(loginRequestDto);
    }

    @DisplayName("토큰 재발급 테스트")
    @Test
    public void reissueTest() throws Exception {
        //given
        TokenRequestDto tokenRequestDto = new TokenRequestDto("access","refresh");

        //when
        mockMvc.perform(
                post("/api/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequestDto)))
                .andExpect(status().isOk());

    }


}
