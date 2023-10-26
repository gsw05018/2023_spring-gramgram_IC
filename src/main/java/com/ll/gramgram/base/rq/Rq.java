package com.ll.gramgram.base.rq;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

@Component // Spring에서 관리하는 Bean으로 지정하는 Component 어노테이션
@RequestScope // 요청 스코프에서 동작하도록 지정하는 RequestScope 어노테이션
public class Rq {
    private final MemberService memberService; // MemberService를 사용하기 위한 멤버 변수
    private final HttpServletRequest req; // HttpServletRequest를 사용하기 위한 멤버 변수
    private final HttpServletResponse resp; // HttpServletResponse를 사용하기 위한 멤버 변수
    private final HttpSession session; // HttpSession을 사용하기 위한 멤버 변수
    private final User user; // User를 사용하기 위한 멤버 변수
    private Member member = null; // Member 객체를 담을 멤버 변수, 초기값은 null

    public Rq(MemberService memberService, HttpSession session, HttpServletRequest req, HttpServletResponse resp){

        this.memberService = memberService; // memberService 초기화
        this.req = req; //  HttpServletRequest 초기화
        this.resp = resp; // HttpServletResponse 초기화
        this.session = session; // HttpSession 초기화

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 로그인한 회원의 인증정보를 가져옴

        if(authentication.getPrincipal() instanceof User){ // 현재 사용자가 User클래스의 인스턴스인지 확인
            this.user = (User) authentication.getPrincipal(); // 사용자가 User 클래스의 인스턴스 일경우 user 변수에 캐스팅하여 할당
        } else {
            this.user = null; // 사용자가 user 클래스의 인스턴스가 아닐 경우 user 변수에 null 할당
        }

    }


    public boolean isLogin(){ // 로그인 상태를 확인하는 메서드
        return user != null; // user 변수가 null 아닐경우 로그인

    }

    public boolean isLogout(){ // 로그아웃 상태를 확인하는 메서드
        return !isLogin(); // 로그인이 아닌경우 로그아웃

    }

    public Member getMember(){ // 로그인된 회원ㄷ의 객체를 반환하는 메서드

        if(isLogout()){
            return null; // 로그아웃 상태일 경우 null 반환
        }

        if(member == null){
            member = memberService.findByUsername(user.getUsername()).orElseThrow();
            // memberService를 사용하여 현재 사용자의 정보를 가져옴
        }
        return member; // 회원정보를 반환

    }

    public String historyBack(String msg) {
        // 사용자가 보낸 메시지를 매개변수로 받아서 처리
        String referer = req.getHeader("referer");
        // 요청에서 referer 헤더를 가져옴
        String key = "historyBackErrorMsg___" + referer;
        // localStorage에 저장될 키를 생성
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        // 생성된 키를 요청의 속성으로 설정
        req.setAttribute("historyBackErrorMsg", msg);
        // 이전 페이지로 돌아갈 때 표시할 메시지를 요청의 속성으로 설정
        return "common/common.js";

    }

    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }
    // 주어진 RsData 객체에서 메시지를 가져와 historyBack 메서드에 전달하여 실행하고 결과를 반환

    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }
    // 주어진 URL가 RsData 객체에서 메시지를 가져와 redirctWithMsg 메서드에 전달하여 결과를 반환

    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }
    // 주어진 URL과 메시지를 조합하여 "redirect:"를 추가하여 리다이렉트할 URL을 반환합니다.

    private String urlWithMsg(String url, String msg) {
        // 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }
    // 기존 URL에 있는 msg파라미터를 삭제하고 새로운 msg파라미터를 추가하여 URL을 수정하여 반환
    // 이때, msg 파라미터에는 ttl을 포함하여 반환

    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }
    // 주어진 메시지를 URL 인코딩하고 현재 시간을 기준으로 ttl 파라미터를 추가하여 반환
}
