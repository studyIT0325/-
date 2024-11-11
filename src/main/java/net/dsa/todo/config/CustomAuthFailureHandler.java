package net.dsa.todo.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
		 * CredentailsExpriedException: UserDetails 객체의 isCredentialNonExpired() 메서드의 반환값이 false일 경우
		 */
		String message = "";
		if(exception instanceof InternalAuthenticationServiceException) {
			log.info("아이디가 없음");
			message = "아이디가 없습니다.";
		} else if(exception instanceof BadCredentialsException) {
			log.info("패스워드 틀림");
			message = "패스워드를 틀렸습니다.";
		} else if(exception instanceof LockedException) {
			log.info("LockedException임");
			message = "계정이 잠겼습니다.";
		} else if(exception instanceof DisabledException) {
			log.info("DisabledException임");
			message = "계정이 비활성화되었습니다.";
		} else if(exception instanceof AccountExpiredException) {
			log.info("AccountExpiredException임");
			message = "계정이 만료되었습니다.";
		} else if(exception instanceof CredentialsExpiredException) {
			log.info("CredentailsExpriedException임");
			message = "비밀번호가 만료되었습니다.";
		} else {
			log.info("알 수 없는 이유로 로그인 실패");
			message = "로그인에 실패했습니다.";
		}
		
		// 로그인 실패시 지정한 URL로 파라미터를 담아서 전달
		String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
		setDefaultFailureUrl("/user/login?message=" + encodedMessage);
		
		// 로그인 실패시 기본 처리 방법
		super.onAuthenticationFailure(request, response, exception);
	}
}
