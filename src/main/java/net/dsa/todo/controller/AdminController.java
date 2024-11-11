package net.dsa.todo.controller;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dsa.todo.model.User;
import net.dsa.todo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;
	
	// 관리자 페이지 메인
	@GetMapping("admin")
	public String admin() {
		log.info("관리자 메인 페이지");
		return "admin/main";
	}
	
	// 관리자 로그인 페이지 이동
	@GetMapping("admin/login")
	public String login() {
		return "admin/login";
	}
	
	// 회원 목록 조회
	@GetMapping("admin/users")
	public String users(Model model) {
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "admin/list";
	}
	
	// 회원 정보 상세
	@GetMapping("admin/users/{id}")
	public String details(@PathVariable(name="id") String id, Model model) {
		User user = userService.findUser(id);
		model.addAttribute("user", user);
		return "admin/edit";
	}
	
	// 회원 정보 수정
	@Secured({"ROLE_MANAGER", "ROLE_ADMIN"}) // 접근 가능한 권한 지정
	@PostMapping("admin/users/{id}/edit")
	public String edit(@PathVariable(name="id") String id, @ModelAttribute User user) {
		userService.updateUser(id, user);
		return "redirect:/admin/users/" + id;
	}
	
	// 회원 정보 삭제
//	@Secured({"ROLE_ADMIN"}) // 접근 가능한 권한 지정
	@PreAuthorize("hasRole('ROLE_ADMIN')") // 접근 가능한 권한 지정
	@GetMapping("admin/users/{id}/delete")
	public String delete(@PathVariable(name="id") String id) {
		userService.deleteUser(id);
		return "redirect:/admin";
	}
}
