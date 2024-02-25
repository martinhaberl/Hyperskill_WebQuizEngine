package training.quizTdd.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(Customizer.withDefaults())     // Default Basic auth config
                .csrf(configurer -> configurer.disable()).headers(cfg -> cfg.frameOptions().disable()) // for POST requests via Postman
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/actuator/shutdown").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                                .requestMatchers("/api/quizzes/*").authenticated()
                                .anyRequest().denyAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
        httpSecurity
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry

                        .requestMatchers(mvc.pattern("/actuator/shutdown")).permitAll()

                );

        return httpSecurity.build();
    }*/
