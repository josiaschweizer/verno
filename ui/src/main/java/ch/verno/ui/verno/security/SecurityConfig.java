package ch.verno.ui.verno.security;

import ch.verno.server.service.intern.AppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static class PublicEndpoints {
    static final String[] API_PUBLIC_POST = {
            "/api/user/register",
            "/api/user/login",
            "/api/user/reset-password",
            "/api/organisation",
            "/api/contact"
    };

    static final String[] API_PUBLIC_GET = {
            "/api/init-data",
            "/api/user/verify"
    };

    static final String[] VAADIN_PUBLIC = {
            "/VAADIN/**",
            "/vaadinServlet/**",

            "/frontend/**",
            "/frontend-es5/**",
            "/frontend-es6/**",

            "/icons/**",
            "/images/**",
            "/img/**",
            "/styles/**",
            "/styles.css",
            "/themes/**",
            "/line-awesome/**",
            "/lumo/**",

            "/manifest.webmanifest",
            "/sw.js",
            "/sw-runtime-resources-precache.js",
            "/offline.html",
            "/offline-stub.html",
            "/favicon.ico",
            "/robots.txt",

            "/webjars/**",

            "/sockjs-node/**"
    };

    static final String[] VAADIN_INTERNAL = {
            "/heartbeat",
            "/heartbeat/**",

            "/UIDL",
            "/UIDL/**",
            "/uidl",
            "/uidl/**",

            "/PUSH",
            "/PUSH/**",
            "/push",
            "/push/**"
    };

    static final String[] ACTUATOR = {
            "/actuator/health",
            "/actuator/health/**"
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  @Bean
  public CustomAuthenticationProvider customAuthenticationProvider(@Nonnull final AppUserService appUserService,
                                                                   @Nonnull final PasswordEncoder encoder) {
    return new CustomAuthenticationProvider(appUserService, encoder);
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(@Nonnull final JwtUtil jwtUtil,
                                                         @Nonnull final AppUserService appUserService) {
    return new JwtAuthenticationFilter(jwtUtil, appUserService);
  }

  @Bean
  public SecurityFilterChain filterChain(@Nonnull final HttpSecurity http,
                                         @Nonnull final CustomAuthenticationProvider customAuthProvider,
                                         @Nonnull final JwtAuthenticationFilter jwtAuthFilter) throws Exception {

    return http
            .cors(Customizer.withDefaults())
            .authenticationProvider(customAuthProvider)
            .csrf(AbstractHttpConfigurer::disable)
//            .csrf(csrf -> csrf
//                    .ignoringRequestMatchers("/api/**")
//            )

            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/", true)
            )

            .logout(logout -> logout
                    .logoutSuccessUrl("/login")
                    .permitAll()
            )

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PublicEndpoints.VAADIN_PUBLIC).permitAll()
                    .requestMatchers(PublicEndpoints.VAADIN_INTERNAL).permitAll()
                    .requestMatchers("/login", "/login/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers(HttpMethod.POST, PublicEndpoints.API_PUBLIC_POST).permitAll()
                    .requestMatchers(HttpMethod.GET, PublicEndpoints.API_PUBLIC_GET).permitAll()
                    .requestMatchers(PublicEndpoints.ACTUATOR).permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().authenticated()
            )

//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
          @Nonnull final AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}