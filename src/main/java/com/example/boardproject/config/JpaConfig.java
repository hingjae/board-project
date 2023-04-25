package com.example.boardproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing //?
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("honey"); // TODO : 스프링 시큐리티로 인증 기능 붙일 때 수정하기 "honey"는 임의로 넣어둔 데이터 createdBy에 들어갈 데이터
    }
}
