package br.com.cc.varzeafc.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceLogin users;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/add", "/cadastro", "/usuario/add").permitAll()
				.antMatchers("/**").hasAnyRole("PRESIDENTE", "PUBLICADOR", "ADMIN")
				.antMatchers("/admin/**").hasAnyRole("ADMIN").anyRequest().authenticated()
				.and().formLogin().loginPage("/login").defaultSuccessUrl("/varzeafc").permitAll().and()
				.rememberMe().and().logout().deleteCookies("JSESSIONID").logoutSuccessUrl("/");

		/*
		 * EXAMPLE OF AUTHENTICATION AND AUTHORIZATION
		 * 
		 * http.authorizeRequests() .antMatchers("/url1").hasRole("ADMIN")
		 * .antMatchers("/url2/**").permitAll()
		 * .antMatchers(HttpMethod.POST,"/specificUrl").hasRole("ADMIN")
		 * .antMatchers("/url3/**").permitAll() .anyRequest().authenticated()
		 * .and() .formLogin().loginPage("/login").permitAll() .and()
		 * .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		 */
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(users).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// you can change
		web.ignoring().antMatchers("/assets/**","/resources/");
	}
}
