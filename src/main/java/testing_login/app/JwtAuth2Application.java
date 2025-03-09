package testing_login.app;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import testing_login.app.entities.AppRole;
import testing_login.app.entities.AppUser;
import testing_login.app.entities.Authority;
import testing_login.app.repository.AuthorityRepository;
import testing_login.app.repository.RoleRepository;
import testing_login.app.repository.UserRepository;

@SpringBootApplication
public class JwtAuth2Application {

	public static void main(String[] args) {
		SpringApplication.run(JwtAuth2Application.class, args);
	}
	
	@Bean
	CommandLineRunner initDatabase(RoleRepository roleRepo, AuthorityRepository authorityRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
	    return args -> {
	        // Create Authorities (Privileges)
	        Authority readPrivilege = Authority.builder().name("READ_PRIVILEGES").build();
	        Authority writePrivilege = Authority.builder().name("WRITE_PRIVILEGES").build();
	        Authority deletePrivilege = Authority.builder().name("DELETE_PRIVILEGES").build();

	        authorityRepo.saveAll(Set.of(readPrivilege, writePrivilege, deletePrivilege));

	        // Create Roles and Assign Privileges
	        AppRole adminRole = AppRole.builder()
	                .role("ADMIN")
	                .authorities(Set.of(readPrivilege, writePrivilege, deletePrivilege))
	                .build();

	        AppRole employerRole = AppRole.builder()
	                .role("EMPLOYER")
	                .authorities(Set.of(readPrivilege))
	                .build();

	        AppRole organismeRole = AppRole.builder()
	                .role("ORGANISME")
	                .authorities(Set.of(readPrivilege, writePrivilege))
	                .build();

	        roleRepo.saveAll(Set.of(adminRole, employerRole, organismeRole));

	        // Create Users with their respective roles
	        AppUser adminUser = AppUser.builder()
	                .firstname("Admin")
	                .lastname("User")
	                .email("admin@gmail.com")
	                .password(passwordEncoder.encode("1234"))
	                .role(adminRole)
	                .build();

	        AppUser employerUser = AppUser.builder()
	                .firstname("Employer")
	                .lastname("User")
	                .email("employer@gmail.com")
	                .password(passwordEncoder.encode("1234"))
	                .role(employerRole)
	                .build();

	        AppUser organismeUser = AppUser.builder()
	                .firstname("Organisme")
	                .lastname("User")
	                .email("organisme@gmail.com")
	                .password(passwordEncoder.encode("1234"))
	                .role(organismeRole)
	                .build();

	        userRepo.saveAll(Set.of(adminUser, employerUser, organismeUser));
	    };
	}

}
