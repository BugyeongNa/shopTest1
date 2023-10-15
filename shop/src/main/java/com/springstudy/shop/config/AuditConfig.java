package com.springstudy.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // JPA의 Auditing기능 활성화
public class AuditConfig {

    // 등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

}

/*
Spring Data Jpa제공하는 Auditing기능
: 엔티티가 저장 또는 수정될 때 자동으로 등록일, 수정일, 등록자, 수정자를 자동으로 입력해주는 기능
Audit->감시하다
엔티티의 생성과 수정을 감시한다는 의미

 */
