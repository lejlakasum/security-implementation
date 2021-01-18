package com.example.ppis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RepositoryAwareUserDetailsService myUserDetailService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(RepositoryAwareUserDetailsService myUserDetailService,
                                 JwtRequestFilter jwtRequestFilter) {
        this.myUserDetailService = myUserDetailService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/user/login", "/api/user/validate")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/csrf",
                        "/").permitAll()

                .antMatchers(HttpMethod.GET, "/api/user/all")
                .hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "/api/user/register")
                .hasAuthority("admin")
                .antMatchers(HttpMethod.DELETE, "/api/user/{id}")
                .hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "/api/user/change-password")
                .hasAnyAuthority("admin", "hr")
                .antMatchers(HttpMethod.GET, "/api/user/{email}")
                .hasAnyAuthority("admin", "hr")

                .antMatchers(HttpMethod.GET, "/api/skill-types")
                .hasAuthority("hr")

                .antMatchers(HttpMethod.GET, "/api/skills")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.POST, "/api/skills")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.DELETE, "/api/skills/{id}")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.GET, "/api/skills/{id}/employees")
                .hasAnyAuthority("hr", "admin")

                .antMatchers(HttpMethod.GET, "/api/employees")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.POST, "/api/employees")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.DELETE, "/api/employees/{id}")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.GET, "/api/employees/{id}/skills")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.POST, "/api/employees/{id}/skills")
                .hasAuthority("hr")

                .antMatchers(HttpMethod.POST, "/api/certificate")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.DELETE, "/api/certificate/{id}")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.GET, "/api/certificate/{id}")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.GET, "/api/certificate/all")
                .hasAnyAuthority("admin","hr")

                .anyRequest()
                .denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
