package com.hunmin.domain.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunmin.domain.dto.member.CustomUserDetails;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.entity.MemberLevel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

// 로그인 요청 처리 클래스
@Log4j2
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/members/login");
    }

    // 요청에서 email 파라미터 추출
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }

    // 위에서 추출한 이메일과 비밀번호 추출하여 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("========= attemptAuthentication 시작 =========");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> requestMap = mapper.readValue(request.getInputStream(), Map.class);
            String email = requestMap.get("email");
            String password = requestMap.get("password");
            log.info("===== 이메일: " + email + " =====");
            log.info("===== 비밀번호: " + password + " =====");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
            log.info("===== 인증 결과: " + authToken + " =====");
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("===== 잘못된 요청 폼 =====");
        }
    }

    // 로그인 성공 시 사용자 정보를 기반으로 JWT 토큰을 생성하고, 이를 Authorization 헤더에 추가
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        log.info("========= successfulAuthentication 시작 =========");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //유저 정보
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority().replace("ROLE_", "");
        log.info("===== Authentication 성공!! email: {}, Role: {}", email, role);
        Long memberId = customUserDetails.getMemberId();
        String nickname = customUserDetails.getNickname();
        String image = "http://localhost:8080" + customUserDetails.getImage();
        MemberLevel level = customUserDetails.getLevel();
        String country = customUserDetails.getCountry();

        //토큰 생성
        String access = jwtUtil.createJwt("access", email, MemberRole.valueOf(role), 600000L);
        String refresh = jwtUtil.createJwt("refresh", email, MemberRole.valueOf(role), 86400000L);
        log.info("생성된 access 토큰: " + access);
        log.info("생성된 refresh 토큰: " + refresh);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + access + "\"," +
                " \"memberId\": " + memberId + ", " +
                "\"role\": \"" + role + "\", " +
                "\"nickname\": \"" + nickname + "\", " +
                "\"image\": \"" + image + "\", " +
                "\"email\": \"" + email + "\", " +
                "\"level\": \"" + level + "\", " +
                "\"country\": \"" + country + "\"}");

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    // 로그인 실패 시 HTTP 응답 401로 설정(유효한 자격 증명 미제공 시 요청 거부)
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(401);
        log.info("===== 인증 실패 =====");
    }

    private Cookie createCookie(String key, String value) {
        // key와 jwt를 매개로 받아 cookie 생성
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); // 쿠키 생명 주기
        // cookie.setSecure(true); // https 사용 시 적용
        // cookie.setPath("/"); // 쿠키 적용 범위
        cookie.setHttpOnly(true); // 자바스크립트로 쿠키에 접근 제한

        return cookie;
    }
}
