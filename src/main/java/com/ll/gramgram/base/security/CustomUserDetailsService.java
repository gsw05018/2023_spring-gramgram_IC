package com.ll.gramgram.base.security;

import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    // UserDetailsService 인터페이스에서 loadUserByUsername 메서드를 재정의한다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        // MemberRepository를 사용하여 제공된 사용자 이름으로 회원찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("username(%s) not found".formatted(username)));
        // 회원을 찾을 수가 없는 경우, 포맷된 오류메시지 출력
        return new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
        // 검색된 회원의 정보를 사용하여 새로운 User 객체를 생성하고 반환
    }

    // security에서는 이걸 우선으로 실행을 해준다!

}
