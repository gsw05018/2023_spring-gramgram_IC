package com.ll.gramgram.base.initData;

import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"}) // dev 또는 test 프로파일일 때 활성화되는 설정 클래스
public class NotProd {

    @Bean
    CommandLineRunner initData(
            MemberService memberService
            // CommandLineRunner을 사용하여 초기 데이터 설정
    ) {
        return args -> {
            // MemberService를 사용하여 각각의 사용자 등록
            memberService.join("admin", "1234");
            memberService.join("user1", "1234");
            memberService.join("user2", "1234");
            memberService.join("user3", "1234");
            memberService.join("user4", "1234");
        };
    }


}