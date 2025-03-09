package testing_login.app.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema="my_schema")
public class AppRole {
	@Id
	@GeneratedValue
	private Long id;
	private String role;
	
	 @ManyToMany(fetch = FetchType.EAGER) // Load authorities when role is loaded
	    @JoinTable(
	        name = "role_authority",
	        joinColumns = @JoinColumn(name = "role_id"),
	        inverseJoinColumns = @JoinColumn(name = "authority_id")
	    )	 
	 private Set<Authority> authorities = new HashSet<>();
}
