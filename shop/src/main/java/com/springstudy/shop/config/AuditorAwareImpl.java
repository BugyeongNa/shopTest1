package com.springstudy.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

/*
- 현재 로그인한 사용자의 정보를 등록자와 수정자로 지정
- AuditorAware인터페이스를 구현한 클래스를 생성
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        // 인증과정이 정상적으로 처리되었을 때(로그인상태)
        if(authentication != null){

            // 현재 로그인 한 사용자의 정보를 조회
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }

}