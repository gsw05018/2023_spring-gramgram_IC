package com.ll.gramgram.boundedContext.member.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // 스프링부트 관련 컴포넌트 테스트할 때 붙여야 함, Ioc 컨테이너 작동시킴
@AutoConfigureMockMvc // http 요청, 응답 테스트
@Transactional // 실제로 테스트에서 발생한 DB 작업이 영구적으로 적용되지 않도록, test + 트랜잭션 => 자동롤백
@ActiveProfiles("test") // application-test.yml 을 활성화 시킨다.
public class MemberControllerTests {
    @Autowired
    private MockMvc mvc; // MockMvc 객체 주입받음

    @Test
    @DisplayName("회원가입 폼") // 테스트 메서드에 대한 설명을 나타내는 애너테이션
    void t001() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join")) // member/join으로 get요청 수행
                .andDo(print()); // 크게 의미 없고, 그냥 확인용

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class)) // 핸들러의 타입 확인
                .andExpect(handler().methodName("showJoin")) // 핸들러의 메서드 이름 확인
                .andExpect(status().is2xxSuccessful()) // HTTP 응답 상태가 2xx성공인지 확인
                .andExpect(content().string(containsString(""" 
                        <input type="text" name="username"
                        """.stripIndent().trim()))) // 응답 본문에 특정 문자열이 포함되어 있는지 확인
                .andExpect(content().string(containsString(""" 
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="회원가입"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("회원가입")
    void t002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join") // member/join으로 post 요청 수행
                        .with(csrf()) // CSRF 키 생성 
                        .param("username", "user10") // 요청에 username 파라미터 추가
                        .param("password", "1234") // 요청에 password 파라미터 추가
                )
                .andDo(print());

        // THEN
        resultActions 
                .andExpect(handler().handlerType(MemberController.class)) // 핸들러의 타입 확인
                .andExpect(handler().methodName("join")) // 핸들러의 메서드 이름 확인
                .andExpect(status().is3xxRedirection()); // HTTP 응답 상태가 3xx 리다이렉션인지 확인
    }


    @Test
    @DisplayName("회원가입시에 올바른 데이터를 넘기지 않으면 400")
    void t003() throws Exception {
        // WHEN
        ResultActions resultActions = mvc // username이 에러
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user10")
                )
                .andDo(print());

        // THEN
        resultActions //username 400에러 발생
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());

        // WHEN
        resultActions = mvc //password 에러
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("password", "1234")
                )
                .andDo(print());

        // THEN
        resultActions // password 400에러 발생
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());

        // WHEN
        resultActions = mvc //username의 길이가 너무 긴 경우
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user10" + "a".repeat(30))
                        .param("password", "1234")
                )
                .andDo(print());

        // THEN
        resultActions // username의 길이가 너무 긴 경우 400 에러 발생
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());

        // WHEN
        resultActions = mvc // password의 길이가 너무 긴 경우 발생
                .perform(post("/member/join")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "user10")
                        .param("password", "1234" + "a".repeat(30))
                )
                .andDo(print());

        // THEN
        resultActions //password의 길이가 너무 긴 경우 에러 400 발생
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().is4xxClientError());
    }
}