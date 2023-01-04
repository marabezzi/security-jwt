package io.github.marabezzi.securityjwt.conf;

import java.util.Arrays;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.github.marabezzi.securityjwt.model.enums.Perfil;
import io.github.marabezzi.securityjwt.service.UsuarioService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
	
	private static final String ADMIN = Perfil.ADMIN.getDescricao();
    private static final String USER = Perfil.USER.getDescricao();
    private static final String DEVELOPER = Perfil.DEVELOPER.getDescricao();
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.authorizeHttpRequests((authorize) -> authorize				
			// acessos públicos liberados
			.requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
			.requestMatchers("/", "/home", "/expired").permitAll()
			
			// acessos privados admin
			.requestMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(USER, DEVELOPER)
			.requestMatchers("/api/usuarios/**").hasAuthority(ADMIN)

			// acessos privados Developer
			.requestMatchers("/developer/especialidade/titulo/*").hasAnyAuthority(DEVELOPER, USER)
			.requestMatchers("/developer/dados", "/developer/salvar", "/developer/editar").hasAnyAuthority(DEVELOPER, ADMIN)
			.requestMatchers("/developer/**").hasAuthority(DEVELOPER)

			// acessos privados user
			.requestMatchers("/api/usuarios/**").hasAuthority(USER)
			

			.anyRequest().authenticated()
		)
		.formLogin()
		/*	.loginPage("/login")
			.defaultSuccessUrl("/api/usuarios", true)
			.failureUrl("/login-error")
			.permitAll()*/
		.and()
			.logout()
			.logoutSuccessUrl("/")
			.deleteCookies("JSESSIONID")
		.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado");
		//.and()
		//	.rememberMe();
		
		/*http.sessionManagement()
				.maximumSessions(1)
				.expiredUrl("/expired")
				.maxSessionsPreventsLogin(false)
				.sessionRegistry(sessionRegistry());

		http.sessionManagement()
				.sessionFixation()
				.newSession()
				.sessionAuthenticationStrategy(sessionAuthStrategy());
		*/
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, 
													   PasswordEncoder passwordEncoder, 
													   UsuarioService userDetailsService) throws Exception {
		
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder)
				.and()
				.build();
	}
	
	@Bean
	SessionAuthenticationStrategy sessionAuthStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}
	
	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<>( new HttpSessionEventPublisher() );
	}
}
