package com.ll.gramgram.boundedContext.instaMember.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.repository.InstaMemberRepositorty;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // 해당 클래스가 서비스임을 나타내는 어노테이션
@RequiredArgsConstructor // 필드 주입을 위한 생성자를 자동으로 생성해주는 롬복 어노테이션
public class InstaMemberService { // InstaMember 서비스 클래스 시작

    private final InstaMemberRepositorty instaMemberRepositorty; // InstaMemberRepository를 주입받는 필드

    public Optional<InstaMember> findByUsername(String username) { // username을 통해 InstaMember를 찾는 메서드
        return instaMemberRepositorty.findByUsername(username); // username으로 인스타멤버를 찾아 반환하는 부분
    }

    public RsData<InstaMember> connect(Member member, String username, String gender) { // 인스타멤버를 연결하는 메서드

        if (findByUsername(username).isPresent()) { // 이미 존재하는 username인지 확인하는 부분
            return RsData.of("F-1", "해당 인스타그램 아이디는 이미 다른 사용자와 연결되었습니다"); // 이미 존재할 경우 에러 메세지 반환
        }
        RsData<InstaMember> instaMemberRsData = create(username, gender); // 연결을 진행하는 메서드 호출
        return instaMemberRsData; // 연결 결과 반환
    }

    private RsData<InstaMember> create(String username, String gender) { // 인스타멤버를 생성하는 메서드
        InstaMember instaMember = InstaMember // 인스타멤버 객체 생성
                .builder() // 빌더 패턴 사용
                .username(username) // username 설정
                .gender(gender) // gender 설정
                .build(); // 빌더로 객체 생성

        instaMemberRepositorty.save(instaMember); // 인스타멤버를 저장하는 부분

        return RsData.of("S-1", "인스타계정이 등록되었습니다", instaMember); // 성공적으로 생성되었을 경우 결과 반환
    }
}

