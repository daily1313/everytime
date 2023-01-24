package com.example.community.controller.auth;


import static com.example.community.response.Response.success;

import com.example.community.dto.jwt.LoginRequestDto;
import com.example.community.dto.jwt.SignUpRequestDto;
import com.example.community.dto.jwt.TokenRequestDto;
import com.example.community.response.Response;
import com.example.community.service.auth.AuthService;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public Response register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return success();
    }

    @ApiOperation(value = "로그인", notes = "로그인을 한다.")
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody LoginRequestDto req) {
        return success(authService.signIn(req));
    }

    @ApiOperation(value = "토큰 재발급", notes = "토큰 재발급 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reissue")
    public Response reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return success(authService.reissue(tokenRequestDto));
    }
}
