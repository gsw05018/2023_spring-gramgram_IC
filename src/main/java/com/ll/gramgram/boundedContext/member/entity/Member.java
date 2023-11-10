package com.ll.gramgram.boundedContext.member.entity;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // @CreateDate, @LastModifyDate가 작동되도록 허용
@ToString
@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate // INSERT할 때 값이 자동으로 들어간다
    private LocalDateTime createDate;
    @LastModifiedDate // UPDATE할 때 값이 자동으로 들어간다
    private LocalDateTime modifyDate;
    @Column(unique = true)
    private String username;
    private String password;

    @OneToOne
    @Setter
    private InstaMember instaMember;

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // GrantedAuthority를 저장할 리스트 생성
        grantedAuthorities.add(new SimpleGrantedAuthority("member"));
        // Member라는 이름의 SimpleGrantedAuthority를 생성하여 리스트 추가
        if ("admin".equals(username)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }
        // Username이 amidn인 경우 admin이라는 이름의 SimpleGrantedAuthority를 리스트에 추가

        return grantedAuthorities;
        // 권한 리스트 반환
    }

    // 메서드 시그니처 : getGrantedAuthorities, 반환형은 List<? esxtends GrantedAuthority>를 리턴
    //SimpleGrantedAuthority : 스프링 시큐리티에서 사용자의 권한을 표현하는데 사용되는 클래스
    // 사용자가 가질 수 있는 권한을 나타내는 문자열을 저장하고, 이를 기반으로 인증 및 권한 부여 처리
    // 간단한 문자열로 표현된 권한을 나타내며, 일반적으로 ROLE_ADMIN같은 형식으로 사용됨

    public boolean hasConnectedInstaMember(){
        return instaMember != null;
    }

}