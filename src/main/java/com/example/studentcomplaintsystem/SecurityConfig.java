
package com.example.studentcomplaintsystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            PasswordEncoder encoder) {

        UserDetails student = User.withUsername("student")
                .password(encoder.encode("Student123!"))
                .roles("STUDENT")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("Admin123!"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(student, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/login", "/register")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/login", "/register")
                .permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/complaints/*/status")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/complaints")
                .hasAnyRole("STUDENT", "ADMIN")
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );

        return http.build();
    }
}
