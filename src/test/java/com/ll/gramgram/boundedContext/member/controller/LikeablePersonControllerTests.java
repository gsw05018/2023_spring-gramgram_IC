package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.boundedContext.likeablePerson.controller.LikeablePersonController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Spring Boot 어플리케이션 컨텍스트를 로드하기 위한 어노테이션
@AutoConfigureMockMvc // MockMvc를 자동 구성하기 위한 어노테이션
@Transactional // 테스트가 끝나면 롤백시키기 위한 어노테이션
@ActiveProfiles("test") // 테스트 프로파일을 설정하기 위한 어노테이션
public class LikeablePersonControllerTests {

    @Autowired
    private MockMvc mvc; // MockMvc 객체를 의존성 주입받는 변수

    // 인스탕회원 정보 입력 폼 테스트
    @Test // 테스트 메서드임을 나타내는 어노테이션
    @DisplayName("등록 폼") // 테스트 케이스의 이름을 정의하는 어노테이션
    @WithUserDetails("user1")
    // 사용자 디테일을 지정하는 어노테이션
    void t001() throws Exception {

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/likeablePerson/add")) // "/likeablePerson/add" 엔드포인트에 GET 요청을 수행하는 MockMvc 객체 생성
                .andDo(print()); // 테스트 결과를 출력하는 동작을 수행

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class)) // 핸들러 타입을 검증하는 조건을 추가
                .andExpect(handler().methodName("showAdd")) // 핸들러 메서드 이름을 검증하는 조건을 추가
                .andExpect(status().is2xxSuccessful()) // HTTP 상태 코드가 2xx(성공)인지 검증하는 조건을 추가
                .andExpect(content().string(containsString("""
                        먼저 본인의 인스타그램 아이디를 입력해주세요
                        """.stripIndent().trim()))) // 응답 본문에 특정 문자열이 포함되어 있는지 검증하는 조건을 추가
        ;
    }
}
