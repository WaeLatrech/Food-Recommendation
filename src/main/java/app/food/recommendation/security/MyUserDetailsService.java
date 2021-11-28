
    
package app.food.recommendation.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.food.recommendation.models.User;
import app.food.recommendation.repositories.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
    private UserRepo userRepo;
    @Override
public UserDetails loadUserByUsername(String email)  throws UsernameNotFoundException {
    	User user =userRepo.findByEmail(email);
        if(user==null)
            throw new UsernameNotFoundException("User Not Found ! ! !");
        return new UserPrincipal(user);
    }

} 






