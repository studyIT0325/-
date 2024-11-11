package net.dsa.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled=true, prePostEnabled=true) // 스프링 시큐리티에서 컨트롤러의 메소드 레벨의 보안을 활성화함
public class SecurityConfig {
	
	private final AuthenticationFailureHandler authenticationFailureHandler;
	
	// 관리자용
	@Bean
	@Order(1) // 생성 순서 설정
	public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable) // csrf 설정을 비활성화
			.securityMatcher("/admin/**"); // /admin/** 경로에 대한 모든 요청을 처리하는 필터로 설정
		
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/admin/login").permitAll() // requestMatchers의 인자로 들어오는 요청은 다 허용함
				.anyRequest().hasAnyRole("MANAGER", "ADMIN")) // 다른 요청은 인증받은 사용자만 허용
			.httpBasic(Customizer.withDefaults()) // httpBasic 인증을 활성화함  
			.formLogin(formLogin -> formLogin // 폼 로그인 방식 사용
				.loginPage("/admin/login") // 사용자가 만든 로그인 페이지를 사용(설정하지 않으면 기본 url이 /login이 됨)
				.usernameParameter("id") // username(아이디) 파라미터 지정(기본값은 username임)
				// .passwordParameter("password") // password(비밀번호) 파라미터 지정(기본값은 password임)
				.loginProcessingUrl("/admin/login") // 로그인 인증 처리를 하는 url 지정
				.defaultSuccessUrl("/admin") // 로그인 성공시 이동할 url
				.failureUrl("/admin/login")) // 로그인 실패시 이동할 url
			.logout(logout -> logout
					.logoutUrl("/admin/logout") // 로그아웃 요청 url
					.logoutSuccessUrl("/admin/login") // 로그아웃 성공시 이동할 url
					.invalidateHttpSession(true) // 세션 클리어
					.deleteCookies("JSESSIONID") // 삭제할 쿠키의 이름 지정
					);
		return http.build();
	}
	
	// 일반 사용자용
	@Bean
	@Order(2) // 생성 순서 설정
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf 설정을 비활성화
		http.csrf(AbstractHttpConfigurer::disable); // (메소드를 값으로 정의해서 메소드를 인수로 전달하는 문법)
		
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/", "/user/register", "/user/login").permitAll() // requestMatchers의 인자로 들어오는 요청은 다 허용함
				.anyRequest().authenticated()) // 다른 요청은 인증받은 사용자만 허용
			.httpBasic(Customizer.withDefaults()) // httpBasic 인증을 활성화함  
			.formLogin(formLogin -> formLogin // 폼 로그인 방식 사용
				.loginPage("/user/login") // 사용자가 만든 로그인 페이지를 사용(설정하지 않으면 기본 url이 /login이 됨)
				.usernameParameter("id") // username(아이디) 파라미터 지정(기본값은 username임)
				// .passwordParameter("password") // password(비밀번호) 파라미터 지정(기본값은 password임)
				.loginProcessingUrl("/user/login") // 로그인 인증 처리를 하는 url 지정
				.defaultSuccessUrl("/user/login-success") // 로그인 성공시 이동할 url
				// .failureUrl("/user/login-fail")) // 로그인 실패시 이동할 url
				.failureHandler(authenticationFailureHandler)) // 로그인 실패 핸들러
			.logout(logout -> logout
					.logoutUrl("/user/logout") // 로그아웃 요청 url
					.logoutSuccessUrl("/") // 로그아웃 성공시 이동할 url
					.invalidateHttpSession(true) // 세션 클리어
					.deleteCookies("JSESSIONID") // 삭제할 쿠키의 이름 지정
					);
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
