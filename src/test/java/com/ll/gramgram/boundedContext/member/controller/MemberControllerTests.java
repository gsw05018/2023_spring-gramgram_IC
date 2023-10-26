package com.ll.gramgram.boundedContext.member.controller;


import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private MemberService memberService;

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
    @Rollback(value = false)  //db에 흔적이 남기위해서 value를 false로해야한다
        // db에 흔적이 남음
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
                .andExpect(status().is3xxRedirection()) // HTTP 응답 상태가 3xx 리다이렉션인지 확인
                .andExpect(redirectedUrlPattern("/member/login?msg=**"));

        Member member = memberService.findByUsername("user10").orElse(null);

        assertThat(member).isNotNull();
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
        resultActions
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

    @Test
    @DisplayName("로그인 폼") // 테스트 메서드에 대한 설명을 나타내는 애너테이션
    void t004() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/login")) // member/join으로 get요청 수행
                .andDo(print()); // 크게 의미 없고, 그냥 확인용

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class)) // 핸들러의 타입 확인
                .andExpect(handler().methodName("showLogin")) // 핸들러의 메서드 이름 확인
                .andExpect(status().is2xxSuccessful()) // HTTP 응답 상태가 2xx성공인지 확인
                .andExpect(content().string(containsString(""" 
                        <input type="text" name="username"
                        """.stripIndent().trim()))) // 응답 본문에 특정 문자열이 포함되어 있는지 확인
                .andExpect(content().string(containsString(""" 
                        <input type="password" name="password"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="submit" value="로그인"
                        """.stripIndent().trim())));
    }

        @Test
        @DisplayName("로그인 처리")
        void t005() throws  Exception{
        // WHEN
            ResultActions resultActions = mvc
                    .perform(post("/member/login")
                            .with(csrf())
                            .param("username", "user1")
                            .param("password", "1234")
                    )
                    .andDo(print());
            // member/login 엔드포인트에 post요청을 보내고 csrf 토큰과 함께 username 및 password 전송
            // 결과 동작을 저장하고, 콘솔에 출력

            MvcResult mvcResult = resultActions.andReturn();
            // ResultActions에서 MvcRsult를 가져옴 이를 통해 응답을 테스트하고 검사 가능
            HttpSession session = mvcResult.getRequest().getSession(false);
            // MvcResult에서 요청을 가져오 ㄴ다음 해당 요청의 세션을 가져온다음 해당 요청의 세션을 가져옴
            // 여기서 false는 세션이 존재하지 않을 경우에는 null 반환
            SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
            // 세션에서 SPRING_SECURITY_CONTEXT 속성을 가져와서 이에 해당하는 SecurityContext를 가져옴
            // 이 보안 컨텍스트에는 사용자 정보와 권한이 포함
            User user = (User)securityContext.getAuthentication().getPrincipal();
            // SecurityContext에서 인증된 사용자를 가져옴
            // 이를 통해 현재 사용자의 정보를 가져올 수 있음
            assertThat(user.getUsername()).isEqualTo("user1");
            // 현재 사용자의 이름이 user1과 동일한지 확인
            // 이를 통해 로그인이 올바르게 처리되었는지 검사가능
            resultActions
                    .andExpect(status().is3xxRedirection())
                    // HTTP 응답의 상태가 3xx Redirection인지 확인
                    .andExpect(redirectedUrlPattern("/**"));
                    // 리다이렉션된 URL이 특정 패턴을 따르는지 확인
        }

    @Test
// @Rollback(value = false) // DB에 흔적이 남는다.
    @DisplayName("로그인 후에 내비바에 로그인한 회원의 username")
    @WithUserDetails("user1") // user1로 로그인 한 상태로 진행
    void t006() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/me"))  // /member/me 엔드포인트에 GET 요청을 보냄
                .andDo(print()); // 요청과 응답을 출력

        // THEN
        resultActions
                .andExpect(handler().handlerType(MemberController.class)) // 처리기 타입이 MemberController 클래스인지 확인
                .andExpect(handler().methodName("showMe")) // 처리기 메서드 이름이 showMe인지 확인
                .andExpect(status().is2xxSuccessful()) // 응답 상태 코드가 2xx(성공)인지 확인
                .andExpect(content().string(containsString("""
                    user1님 환영합니다.
                    """.stripIndent().trim()))); // 응답 콘텐츠에 "user1님 환영합니다." 문자열이 포함되어 있는지 확인
    }

}