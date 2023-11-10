package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final Rq rq;

    @AllArgsConstructor
    @Getter
    public static class JoinForm {
        @NotBlank
        @Size(min = 4, max = 30)
        private final String username;

        @NotBlank
        @Size(min = 4, max = 30)
        private final String password;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }


    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {

        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword());

        if (joinRs.isFail()) {
            return rq.historyBack(joinRs);
        }

        return rq.redirectWithMsg("/member/login", joinRs);
    }
    // 경로에 한글을 넣기 위해서는 encode를 넣어야되고 위 문법을 써야 한다


    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe() {

        return "usr/member/me";
    }
}