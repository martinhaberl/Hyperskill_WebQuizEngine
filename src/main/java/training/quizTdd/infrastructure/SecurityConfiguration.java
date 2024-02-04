package training.quizTdd.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
        httpSecurity
                .httpBasic(Customizer.withDefaults())     // Default Basic auth config
                .csrf(configurer -> configurer.disable()).headers(cfg -> cfg.frameOptions().disable()) // for POST requests via Postman
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .requestMatchers(mvc.pattern("/api/register")).permitAll()
                        .requestMatchers(mvc.pattern("/actuator/shutdown")).permitAll()
                        .requestMatchers(mvc.pattern("/api/quizzes")).authenticated()
                        .anyRequest().denyAll()
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
