package com.hunmin.domain.jwt;

import com.hunmin.domain.dto.member.CustomUserDetails;
import com.hunmin.domain.entity.Member;
import com.hunmin.domain.entity.MemberRole;
import com.hunmin.domain.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Log4j2
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    public JWTFilter(JWTUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.debug("========= TOKEN IS NULL =========");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            log.debug("========= TOKEN EXPIRED =========");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        log.debug("===== Extracted Email: " + email);
        log.debug("===== Extracted Role: " + role);

        Member findMember = memberService.findByEmail(email);
        if (findMember == null) {
            log.debug("========= MEMBER NOT FOUND IN DATABASE =========");
            filterChain.doFilter(request, response);
            return;
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(findMember);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

        log.info("============= Request URI: " + request.getRequestURI());
    }
}
