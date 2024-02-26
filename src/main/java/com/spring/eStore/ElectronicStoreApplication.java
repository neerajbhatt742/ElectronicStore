package com.spring.eStore;

import com.spring.eStore.entity.Role;
import com.spring.eStore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository;
	@Value("${normal.role.id}")
	private String normalRoleId;
	@Value("${admin.role.id}")
	private String adminRoleId;
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String ...args) {
		try {
			Role roleAdmin = Role.builder()
					.roleId(adminRoleId)
					.roleName("ROLE_ADMIN")
					.build();
			Role roleNormal = Role.builder()
					.roleId(normalRoleId)
					.roleName("ROLE_NORMAL")
					.build();
			roleRepository.save(roleAdmin);
			roleRepository.save(roleNormal);

		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
