package com.hunmin.domain.config;

import com.hunmin.domain.jwt.JWTFilter;
import com.hunmin.domain.jwt.JWTUtil;
import com.hunmin.domain.jwt.LoginFilter;
import com.hunmin.domain.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

// Spring Security 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // AuthenticationConfiguration과 JWT 유틸리티 초기화
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    // AuthenticationManager를 Bean으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 비밀번호 암호화를 위한 BCryptPasswordEncoder Bean 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain 체인 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MemberService memberService) throws Exception {

        AuthenticationManager authManager = authenticationManager(authenticationConfiguration);

        LoginFilter loginFilter = new LoginFilter(authManager, jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/members/login");

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                    return configuration;
                })))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/members/register").permitAll()
                        .requestMatchers("/api/members/login").permitAll()
                        .requestMatchers("/main").permitAll()
                        .requestMatchers("/api/members/admin").hasRole("ADMIN")
                        .requestMatchers("/api/chat-room/**").permitAll()//나중에 삭제할것
                        .requestMatchers("/api/chat/**").permitAll()//나중에 삭제할것
                        .requestMatchers("/api/chat/message").permitAll()//나중에 삭제할것
                        .requestMatchers("/api/notification/**").permitAll() //알림 실시간 반영 위한 수정
                        .requestMatchers("/api/board/uploadImage/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll() //게시글 작성 시 이미지
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll() //swagger
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil, memberService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}