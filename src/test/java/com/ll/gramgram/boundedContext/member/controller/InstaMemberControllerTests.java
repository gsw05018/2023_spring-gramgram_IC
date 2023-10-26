package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.boundedContext.instaMember.controller.InstaMemberController;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Spring Boot 어플리케이션 컨텍스트를 로드하기 위한 어노테이션
@AutoConfigureMockMvc // MockMvc를 자동 구성하기 위한 어노테이션
@Transactional // 테스트가 끝나면 롤백시키기 위한 어노테이션
@ActiveProfiles("test") // 테스트 프로파일을 설정하기 위한 어노테이션
public class InstaMemberControllerTests {

    @Autowired
    private MockMvc mvc; // MockMvc 객체를 의존성 주입받는 변수

    @Autowired
    private InstaMemberService instaMemberService;

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

    @Test // 테스트 메서드임을 나타내는 어노테이션
    @DisplayName("인스타회원 정보 입력 폼 처리") // 테스트의 이름을 표시하는 어노테이션
    @WithUserDetails("user1") // 지정된 사용자로 인증된 상태에서 테스트를 실행하는 어노테이션
    @Rollback(value = false) // 롤백을 수행하지 않고 테스트 데이터를 데이터베이스에 반영하는 옵션을 설정하는 어노테이션
    void t003() throws Exception { // 테스트 메서드 시작

        // WHEN 섹션, 테스트할 코드의 실행 부분
        ResultActions resultActions = mvc
                .perform(post("/instaMember/connect") // POST 요청을 수행하는 부분
                        .with(csrf()) // CSRF 토큰을 요청에 추가하는 부분
                        .param("username", "abc123") // 요청 파라미터에 "username"과 값을 추가하는 부분
                        .param("gender", "W") // 요청 파라미터에 "gender"와 값을 추가하는 부분
                )
                .andDo(print()); // 요청 및 응답 정보를 출력하는 부분

        // THEN 섹션, 테스트 결과를 검증하는 부분
        resultActions
                .andExpect(handler().handlerType(InstaMemberController.class)) // 핸들러의 타입을 검증하는 부분
                .andExpect(handler().methodName("connect")) // 핸들러의 메서드 이름을 검증하는 부분
                .andExpect(status().is3xxRedirection()) // 응답 상태 코드가 3xx 리다이렉션인지 확인하는 부분
                .andExpect(redirectedUrlPattern("/pop**")); // 리다이렉트된 URL이 "/pop"으로 시작하는지 확인하는 부분

        InstaMember instaMember = instaMemberService.findByUsername("abc123").orElse(null); // "abc123" 유저네임을 가진 인스타멤버를 찾는 부분

        assertThat(instaMember).isNotNull(); // 찾은 인스타멤버가 null이 아닌지 검증하는 부분

    } // 테스트 메서드 종료

}
