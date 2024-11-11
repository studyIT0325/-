package net.dsa.todo.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	// 사용자 인증 실패시 실행되는 핸들러
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("onAuthenticationFailure 호출");
		log.info("예외: {}", exception);
		/*
		 * 인증 실패의 예외 종류
		 * BadCredentialException: 아이디 또는 패스워드가 다름
		 * LockedException: UserDetails 객체의 isAccountNonLocked() 메서드의 반환값이 false일 경우
		 * DisabledException: UserDetails 객체의 isEnabled() 메서드의 반환값이 false일 경우
		 * AccountExpiredException: UserDetails 객체의 isAccountNonExpired() 메서드의 반환값이 false일 경우
		 * CredentailExprieException: UserDetails 객체의 isCredentialNonExpired() 메서드의 반환값이 false일 경우
		 */
		super.onAuthenticationFailure(request, response, exception);
	}
}
