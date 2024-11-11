package net.dsa.todo.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.ToString;
import net.dsa.todo.model.User;

@Getter
@ToString
public class AuthenticatedUser implements UserDetails {
	
	private User user;
	
	public AuthenticatedUser(User user) {
		this.user = user;
	}
	
	// 인증(Authentication): 
	// 인가(): 

	// 사용자가 가진 권한 리스트를 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new SimpleGrantedAuthority(user.getRole().name()));
		
		return collect;
	}

	// 비밀번호를 반환
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	// 아이디를 반환
	@Override
	public String getUsername() {
		return user.getId();
	}
	
	// 계정의 유효 기간 만료 여부 반환
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// 계정이 잠겨있지 않은지 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	// 계정의 인증 시간 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정 활성화 여부 반환
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
