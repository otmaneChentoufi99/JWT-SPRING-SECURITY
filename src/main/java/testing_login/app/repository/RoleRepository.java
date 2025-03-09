package testing_login.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import testing_login.app.entities.AppRole;

public interface RoleRepository extends JpaRepository<AppRole,Long> {
	Optional<AppRole> findByRole(String role);
}