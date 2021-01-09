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
                .antMatchers(HttpMethod.POST, "/user/login", "/user/validate")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/category", "/category/*",
                        "/subcategory", "/subcategory/*",
                        "/product", "/product/*", "/product/*/*",
                        "/image", "/image/*",
                        "/shop/*")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/csrf",
                        "/").permitAll()

                .antMatchers(HttpMethod.GET, "/user/all")
                .hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "/user/register")
                .hasAuthority("admin")
                .antMatchers(HttpMethod.DELETE, "/user/{id}")
                .hasAuthority("admin")
                .antMatchers(HttpMethod.POST, "/user/change-password")
                .hasAnyAuthority("admin", "hr")
                .antMatchers(HttpMethod.GET, "/user/{email}")
                .hasAnyAuthority("admin", "hr")

                .antMatchers(HttpMethod.GET, "/skill-types")
                .hasAuthority("hr")

                .antMatchers(HttpMethod.GET, "/skills")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.POST, "/skills")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.DELETE, "/skills/{id}")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.GET, "/skills/{id}/employees")
                .hasAnyAuthority("hr", "admin")

                .antMatchers(HttpMethod.GET, "/employees")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.POST, "/employees")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.DELETE, "/employees/{id}")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.GET, "/employees/{id}/skills")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.POST, "/employees/{id}/skills")
                .hasAuthority("hr")

                .antMatchers(HttpMethod.POST, "/certificate")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.DELETE, "/certificate/{id}")
                .hasAuthority("hr")
                .antMatchers(HttpMethod.GET, "/certificate/{id}")
                .hasAnyAuthority("admin","hr")
                .antMatchers(HttpMethod.GET, "/certificate/all")
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
