package app.food.recommendation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class AppSecurityConfig extends WebSecurityConfigurerAdapter{
	//first it checks this file
	@Autowired
	private UserDetailsService UserDetailsService;
	//ctrl shift o
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationProvider authProvider() {
		//fetching data from database
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(UserDetailsService);
		//provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/scss/**","/static/**")
		.permitAll()
		.antMatchers("/user/home").hasAnyAuthority("ADMIN","USER","NOTVERIFIED")
		.antMatchers("/user/add-product").hasAnyAuthority("ADMIN","USER","NOTVERIFIED")
		.antMatchers("/user/**").hasAnyAuthority("ADMIN","USER")
		.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginPage("/Login").permitAll()
        .usernameParameter("email")
        //.defaultSuccessUrl("/user/user", true) //lazem taba3thek win konet ou par default l home
        .defaultSuccessUrl("/default")
        .and()
		.logout().invalidateHttpSession(true)
		.clearAuthentication(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/logout-Success").permitAll();
		http.headers().frameOptions().disable();

	}
	
}
