package com.xmap.v04.config.security;

import com.xmap.v04.entity.User;
import com.xmap.v04.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

//import static java.util.List.of;
//import static java.util.Optional.ofNullable;
//import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
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
        User user = userService.loadUserByUsername((jwtUtil.getUsername(access_token)));

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
