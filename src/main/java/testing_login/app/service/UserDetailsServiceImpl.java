package testing_login.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import testing_login.app.entities.AppUser;
import testing_login.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	 	private final UserRepository usersRepo;
		
	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    	AppUser user = usersRepo.findByEmail(username).orElseThrow();
	    	return user;   
	    }
}