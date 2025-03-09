package testing_login.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import testing_login.app.entities.Authority;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {

}
