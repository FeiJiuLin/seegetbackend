package com.xmap.ar.config.security;

import com.xmap.ar.entity.User;
import com.xmap.ar.service.UserService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain) throws ServletException, IOException {
//
//    }

    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, javax.servlet.FilterChain chain) throws javax.servlet.ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String access_token = header.split(" ")[1].trim();
        if (!jwtUtil.validate(access_token)) {
            chain.doFilter(request, response);
            return;
        }

        //TODO distinct refresh token between access token
        if (Objects.equals(jwtUtil.getIssuer(access_token), "refresh")) {
            chain.doFilter(request, response);
            return;
        }
        // Get user identity and set it on the spring security context
        User user = userService.loadUserByUsername((jwtUtil.getPhone(access_token)));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user, null,
                user.getAuthorities()
        );

        authentication
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}

