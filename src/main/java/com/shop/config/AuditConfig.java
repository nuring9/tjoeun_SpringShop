package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration  // 이 클래스 설정이다
@EnableJpaAuditing // 사용한다 JPA Auditing
public class AuditConfig {
 @Bean  // spring 컨테이너에 등록한다.
    public AuditorAware<String> auditorProvider(){
     return new AuditorAwarelmpl();
 }
}
