package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.boundedContext.instaMember.controller.InstaMemberController; // 인스타 멤버 컨트롤러를 사용하기 위한 import문
import org.junit.jupiter.api.DisplayName; // 테스트 케이스의 이름을 정의하기 위한 import문
import org.junit.jupiter.api.Test; // 테스트 케이스를 생성하기 위한 import문
import org.springframework.beans.factory.annotation.Autowired; // 의존성 주입을 위한 import문
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // MockMvc를 자동 구성하기 위한 import문
import org.springframework.boot.test.context.SpringBootTest; // 테스트를 위한 Spring Boot 어플리케이션 컨텍스트를 로드하기 위한 import문
import org.springframework.security.test.context.support.WithUserDetails; // 테스트에 사용될 사용자의 디테일을 지정하기 위한 import문
import org.springframework.test.context.ActiveProfiles; // 테스트 프로파일을 설정하기 위한 import문
import org.springframework.test.web.servlet.MockMvc; // Spring MVC 테스트를 위한 가짜 객체를 생성하기 위한 import문
import org.springframework.test.web.servlet.ResultActions; // Spring MVC 테스트 결과를 나타내기 위한 import문
import org.springframework.transaction.annotation.Transactional; // 테스트가 끝나면 롤백시키기 위한 import문

import static org.hamcrest.Matchers.containsString; // 문자열에 특정 문자열이 포함되어 있는지 확인하기 위한 import문
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // GET 요청을 보내기 위한 import문
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; // 테스트 결과를 출력하기 위한 import문
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // 특정 조건에 맞는 결과를 기대하는 import문

@SpringBootTest // Spring Boot 어플리케이션 컨텍스트를 로드하기 위한 어노테이션
@AutoConfigureMockMvc // MockMvc를 자동 구성하기 위한 어노테이션
@Transactional // 테스트가 끝나면 롤백시키기 위한 어노테이션
@ActiveProfiles("test") // 테스트 프로파일을 설정하기 위한 어노테이션
public class InstaMemberControllerTests {

    @Autowired
    private MockMvc mvc; // MockMvc 객체를 의존성 주입받는 변수

    // 인스탕회원 정보 입력 폼 테스트
    @Test // 테스트 메서드임을 나타내는 어노테이션
    @DisplayName("인스탕회원 정보 입력 폼") // 테스트 케이스의 이름을 정의하는 어노테이션
    @WithUserDetails("user1") // 사용자 디테일을 지정하는 어노테이션
    void t001() throws Exception {

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/instaMember/connect")) // "/instaMember/connect" 엔드포인트에 GET 요청을 수행하는 MockMvc 객체 생성
                .andDo(print()); // 테스트 결과를 출력하는 동작을 수행

        // THEN
        resultActions
                .andExpect(handler().handlerType(InstaMemberController.class)) // 핸들러 타입을 검증하는 조건을 추가
                .andExpect(handler().methodName("showConnect")) // 핸들러 메서드 이름을 검증하는 조건을 추가
                .andExpect(status().is2xxSuccessful()) // HTTP 상태 코드가 2xx(성공)인지 검증하는 조건을 추가
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim()))) // 응답 본문에 특정 문자열이 포함되어 있는지 검증하는 조건을 추가
                .andExpect(content().string(containsString("""
                        <input type="radio" name="gender" value="W"
                        """.stripIndent().trim()))) // 응답 본문에 특정 문자열이 포함되어 있는지 검증하는 조건을 추가
                .andExpect(content().string(containsString("""
                        <input type="radio" name="gender" value="M"
                        """.stripIndent().trim()))) // 응답 본문에 특정 문자열이 포함되어 있는지 검증하는 조건을 추가
                .andExpect(content().string(containsString("""
                        <input type="submit" value="정보입력"
                        """.stripIndent().trim()))); // 응답 본문에 특정 문자열이 포함되어 있는지 검증하는 조건을 추가
    }

    // 로그인하지 않고 인스타회원 정보 입력 페이지에 접근하는 경우 테스트
    @Test // 테스트 메서드임을 나타내는 어노테이션
    @DisplayName("로그인을 안하고 인스타회원 정보 입력 페이지에 접근하면 로그인 페이지로 302") // 테스트 케이스의 이름을 정의하는 어노테이션
    void t002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/instaMember/connect")) // "/instaMember/connect" 엔드포인트에 GET 요청을 수행하는 MockMvc 객체 생성
                .andDo(print()); // 테스트 결과를 출력하는 동작을 수행

        // THEN
        resultActions
                .andExpect(handler().handlerType(InstaMemberController.class)) // 핸들러 타입을 검증하는 조건을 추가
                .andExpect(handler().methodName("showConnect")) // 핸들러 메서드 이름을 검증하는 조건을 추가
                .andExpect(status().is3xxRedirection()) // HTTP 상태 코드가 3xx(리다이렉션)인지 검증하는 조건을 추가
                .andExpect(redirectedUrlPattern("**/member/login**")); // 리다이렉트된 URL 패턴을 검증하는 조건을 추가
    }

    @Test
    @DisplayName("인스타회원 정보 입력 폼 처리")
    @WithUserDetails("user1")
    void t003() throws Exception{

        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/instaMember/connect")
                        .with(csrf())
                        .param("username", "abc123")
                        .param("gender","W")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(InstaMemberController.class))
                .andExpect(handler().methodName("connect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/pop**"));

    }
}
