package space.yogeshwarank.studysync.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import space.yogeshwarank.studysync.security.JwtAuthenticationFilter;

/**
 * Gives the custom configurations of the spring security system
 *
 * <p>Used to modify the security configurations of our spring security backend system.
 * And expose any secure dependenies for our configurations</p>
 *
 * @author Yogeshwaran K
 * @since 17.09.2026
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    /**
     * This method configures the security filter chain of the studysync backend system
     *
     *It does
     * <li>
     *     <ol>Disables the csrf</ol>
     *     <ol>Exposes some api endpoints as public for accessing without authentication</ol>
     * </li>
     *
     * @param http
     * @return securityFilterChain
     *
     * @since  17.09.2026
     * @author Yogeshwaran K
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.csrf(csrf -> csrf.disable())
                . sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                . authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/register",
                                        "/api/auth/login",
                                        "/actuator/health",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configurs the BCrypt as the password encoder
     */

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
