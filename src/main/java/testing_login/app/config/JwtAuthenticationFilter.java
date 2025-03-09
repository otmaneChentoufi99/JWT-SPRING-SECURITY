package testing_login.app.config;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import testing_login.app.service.JwtService;
import testing_login.app.service.UserDetailsServiceImpl;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		System.out.println("intercept the resquets");
		
		if (request.getServletPath().contains("/api-auth/authenticate") ) {
		      filterChain.doFilter(request, response);
		      return;
		    }
		
		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
	    final String userEmail;
	    
	    if(authHeader == null ||!authHeader.startsWith("Bearer ")) {
	    	filterChain.doFilter(request,response);
	    	return;
	    }
	    
	    jwtToken = authHeader.substring(7);
	    userEmail = jwtService.extractUsername(jwtToken);
	    
	    if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	    	
	    	UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
	    	
	    	if(jwtService.isTokenValid(jwtToken, userDetails)) {
	    		
	    		SecurityContext context = SecurityContextHolder.createEmptyContext();
	    		
	    		UsernamePasswordAuthenticationToken token =
				    				          new UsernamePasswordAuthenticationToken(
				    				        		  userDetails,
				    				        		  null,
				    				        		  userDetails.getAuthorities()
				    				        		);
	    		context.setAuthentication(token);
	            SecurityContextHolder.setContext(context);
	    	}
	    }
	    filterChain.doFilter(request, response);
	}
}
