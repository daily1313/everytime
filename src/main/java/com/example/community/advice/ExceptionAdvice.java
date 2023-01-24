package com.example.community.advice;


import com.example.community.exception.BoardNotFoundException;
import com.example.community.exception.LoginFailureException;
import com.example.community.exception.UserNotFoundException;
import com.example.community.exception.UsernameAlreadyExistException;
import com.example.community.exception.WriterNotFoundException;
import com.example.community.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response userNotFoundException() {return Response.failure(404, "유저를 찾을 수 없습니다.");}
    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response boardNotFoundException()
    {
        return Response.failure(404,"게시글을 찾을 수 없습니다.");
    }

    @ExceptionHandler(WriterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response writerNotFoundException()
    {
        return Response.failure(404,"작성자를 찾을 수 없습니다.");
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException e) { // 2
        return Response.failure(400, e.getBindingResult().getFieldError().getDefaultMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class) // 지정한 예외가 발생하면 해당 메소드 실행
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 각 예외마다 상태 코드 지정
    public Response illegalArgumentExceptionAdvice(IllegalArgumentException e) {
        log.info("e = {}", e.getMessage());
        return Response.failure(500, e.getMessage().toString());
    }

    // 401 응답
    // 아이디 혹은 비밀번호 오류시 발생
    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return Response.failure(401, "로그인에 실패하였습니다.");
    }

    // 409 응답
    // username 중복
    @ExceptionHandler(UsernameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(UsernameAlreadyExistException e) {
        return Response.failure(409, e.getMessage() + "은 중복된 아이디 입니다.");
    }

}
