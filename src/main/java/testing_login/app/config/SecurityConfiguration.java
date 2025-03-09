package testing_login.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
     http
             .csrf(csrf -> csrf.disable())
             .authorizeHttpRequests(authorize -> authorize
            	        .requestMatchers("/api-auth/register").hasAuthority("WRITE_PRIVILEGES") // Only ADMIN register users
            	        .requestMatchers("/api-auth/authenticate").permitAll() // Allow authentication for everyone
            	        .anyRequest().authenticated()
            	    )
             .sessionManagement(session -> session
                             .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
             )
             .authenticationProvider(authenticationProvider)
             .addFilterBefore(
            		 jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                     )
             .exceptionHandling(exceptionHandling -> 
             exceptionHandling.accessDeniedPage("/access-denied"));// Set the access denied page
        return http.build();
    }
}
