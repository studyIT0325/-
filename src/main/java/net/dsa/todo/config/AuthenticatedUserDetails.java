package net.dsa.todo.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dsa.todo.model.User;
import net.dsa.todo.repository.UserRepository;

/*
 * 1. 사용자가 로그인 페이지에서 로그인 시도를 하면 시큐리티 필터 체인에서 로그인 요청을 가로챔
 * 2. UserDetailsService 객체를 찾아서 loadUserByUsername 메소드를 호출함
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserDetails implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("username: {}", username);
		Optional<User> findUser = userRepository.findById(username);
		log.info("findUser: {}", findUser.get());
		
//		if(findUser.isPresent()) return new AuthenticatedUser(findUser.get());
//		return null;
		User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
//		User user = userRepository.findById(username).orElseThrow(() -> new RuntimeException(username + "을 찾을 수 없습니다."));
		
		return new AuthenticatedUser(user);
	}

}
