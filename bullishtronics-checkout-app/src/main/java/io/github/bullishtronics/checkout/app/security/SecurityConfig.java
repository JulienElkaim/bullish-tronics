package io.github.bullishtronics.checkout.app.security;

import io.github.bullishtronics.checkout.app.security.impl.StringEncrypterImpl;
import io.github.bullishtronics.checkout.app.security.impl.UserDetailsServiceImpl;
import io.github.bullishtronics.checkout.core.user.StringEncrypter;
import io.github.bullishtronics.checkout.core.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()

                .antMatchers("/swagger-ui/index.html", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**")
                .permitAll()

                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(UserService userService) {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public StringEncrypter stringEncrypter(PasswordEncoder passwordEncoder) {
        return new StringEncrypterImpl(passwordEncoder);
    }
}
