package com.xmap.ar.config.security;

import com.xmap.ar.mapper.UserMapper;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-ui/index.html",
            "doc.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            "/api/"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userMapper::findUserByPhone);
    }

    // Set password encoding schema
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            System.out.println(ex.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        }
                )
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Our public endpoints
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/api/user/refreshtoken/**").permitAll()
                .antMatchers("/api/data/detail/**").permitAll()
                .antMatchers("/api/data/download/**").permitAll()
                .antMatchers("/redis/**").permitAll()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/role/**").hasRole("ADMIN")
                .antMatchers("/api/test/**").permitAll()
                .antMatchers("/api/minio/**").permitAll()
//                .antMatchers("/api/datapackage").permitAll()
                // Our private endpoints
                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Expose authentication manager bean
    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//        return http.formLogin(Customizer.withDefaults()).build();
//    }
}

