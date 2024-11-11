package net.dsa.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.dsa.todo.model.RoleType;
import net.dsa.todo.model.User;
import net.dsa.todo.repository.UserRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// 회원가입
	@Transactional
	public void register(User user) {
		// 동일한 아이디로 가입된 회원정보가 존재하는지 확인한다.
		Optional<User> findUser = userRepository.findById(user.getId());

		// 회원정보가 존재하지 않으면 가입시킨다.
		if (findUser.isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword())); // 암호화해서 password를 설정
			user.setRole(RoleType.ROLE_USER); // 일반 사용자 권한 부여
			userRepository.save(user);
		}
	}

	// 아이디로 회원정보 조회
	public User findUser(String id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("회원정보가 없습니다."));
	}

	// 모든 회원 정보 조회
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
	
	// 회원 정보 수정
	@Transactional
	public void updateUser(String id, User updateUser) {
		User findUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 없어요."));
		// 비밀번호 정보가 없으면 수정하지 않음
		if(!updateUser.getPassword().isEmpty()) findUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
		findUser.setName(updateUser.getName());
		findUser.setEmail(updateUser.getEmail());
		findUser.setRole(updateUser.getRole());
	}
	
	// 회원 정보 삭제
	@Transactional
	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}
}
