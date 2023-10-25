package com.ll.gramgram.boundedContext.home.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String showMain(){
        return "usr/home/main";
    }

    @GetMapping("/debugSession")
    @ResponseBody
    public String showDebugSession(HttpSession session){
        // HttpSession 객체를 매개변수로 받아 세션 정보를 디버깅하는 메서드
        StringBuilder sb = new StringBuilder("Session content:\n");
        // StringBuilder 객체를 생성하고 초기 텍스트 추가

        Enumeration<String> attributeNames = session.getAttributeNames();
        // 현재 세션에 저장된 속성의 이름 목록을 가져옴

        while (attributeNames.hasMoreElements()){
            // 속성 이름 목록 순회
            String attributeName = attributeNames.nextElement();
            // 다음 세션 속성 이름을 가져옴
            Object attributeValue = session.getAttribute(attributeName);
            // 해당 세션 속성의 값을 가져옴
            sb.append(String.format("%s: %s\n", attributeName, attributeValue));
            // 세션 내용을 문자열로 연결
        }
        return sb.toString();
        // 세션 정보를 문자열 형태로 반환
    }
    // 현재 세션의 저장된 속성들을 읽고, 그 앖을 문자열로 포매팅하여 반환
    // StringBuilder는 문자열 연산을 위한 유용한 클래스이며, Enumeration은 컬렉션 데이터를 열거하기 위한 인터페이스
}
