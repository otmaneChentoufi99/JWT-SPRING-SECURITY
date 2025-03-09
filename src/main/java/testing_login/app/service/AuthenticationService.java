package testing_login.app.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import testing_login.app.dto.AuthenticationRequest;
import testing_login.app.dto.AuthenticationResponse;
import testing_login.app.dto.RegisterRequest;
import testing_login.app.entities.AppRole;
import testing_login.app.entities.AppUser;
import testing_login.app.repository.RoleRepository;
import testing_login.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	  private final UserRepository userRepository;
	  private final RoleRepository roleRepository;
	  private final PasswordEncoder passwordEncoder;
	  private final JwtService jwtService;
	  private final AuthenticationManager authenticationManager;

	  
	  public AuthenticationResponse register(RegisterRequest request) {
		  
		// Check if email already exists
		if (userRepository.existsByEmail(request.getEmail())) {
			return AuthenticationResponse.builder()
		            .error("Email is already Exist")
		            .build();
	    }
		 
		// Fetch role from DB
		AppRole userRole = roleRepository.findByRole(request.getRole()) 
			        .orElseThrow(() -> new RuntimeException("Role not found"));

		  
	    var user = AppUser.builder()
	        .firstname(request.getFirstname())
	        .lastname(request.getLastname())
	        .email(request.getEmail())
	        .password(passwordEncoder.encode(request.getPassword()))
	        .role(userRole)
	        .build();
	    var savedUser = userRepository.save(user);
	    var jwtToken = jwtService.generateToken(user);
	    
	    return AuthenticationResponse.builder()
	            .accessToken(jwtToken)
	        .build();
	    
	  }
	  
	  public AuthenticationResponse authenticate(AuthenticationRequest request) {
		  
		  try {
		        authenticationManager.authenticate(
		            new UsernamePasswordAuthenticationToken(
		                request.getEmail(),
		                request.getPassword()
		            )
		        );
		    } catch (AuthenticationException ex) {
		        return AuthenticationResponse.builder()
		                .error("Invalid credentials")
		                .build();
		    }
		  
		    var user = userRepository.findByEmail(request.getEmail())
		        .orElseThrow();
		    var jwtToken = jwtService.generateToken(user);
		   
		    return AuthenticationResponse.builder()
		            .accessToken(jwtToken)
		            .build();
		  }
}
