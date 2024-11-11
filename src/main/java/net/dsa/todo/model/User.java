package net.dsa.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="tb_user")
public class User {
	
	@Id
	private String id;
	private String password;
	private String name;
	private String email;
	
	@Enumerated(EnumType.STRING) // Enum을 숫자가 아니라 문자로 저장
	private RoleType role;
}
