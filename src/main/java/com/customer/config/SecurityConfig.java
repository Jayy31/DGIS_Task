package com.customer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.customer.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
	private CustomUserDetailsService customUserDetailsServices;


    @Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeHttpRequests()
		.requestMatchers("/user/register","/customers/**", "/user/login").permitAll()	
				.anyRequest()
				.authenticated()
				.and()
				.formLogin()
				.loginPage("/user/login")
				.loginProcessingUrl("/user/login")
				.defaultSuccessUrl("/customers/list", true).permitAll()
		        .and()
		        .logout()
		        .invalidateHttpSession(true)
		        .clearAuthentication(true)
		        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL for logout
		        .logoutSuccessUrl("/user/login?logout") // Redirect URL after logout
		        .permitAll();
		return http.build();
	}
  
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsServices).passwordEncoder(passwordEncoder());
	}

}
//@Autowired
//private UserDetailsService userDetailsService;
//

//@SuppressWarnings("removal")
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//	
//	http.csrf(c->c.disable())
//	
//	.authorizeHttpRequests(request->
//			request
//				.requestMatchers("/register","/customers/**", "/login").permitAll()
//				.anyRequest().authenticated()
//				)
//			.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/customers/list")
//			.permitAll())
//			.logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
//					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//					.logoutSuccessUrl("/login?logout").permitAll())
//			;
//	return http.build();
//			
//}

//@Autowired
//public void configure(AuthenticationManagerBuilder auth) throws Exception{
//  auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
//}

//@SuppressWarnings({ "deprecation", "removal" })
//protected void configure(HttpSecurity http) throws Exception {
//  http.authorizeHttpRequests()
//      .requestMatchers("/register", "/login").permitAll()  // Allow access to sign-up and login pages
//      .anyRequest().authenticated()  // Require authentication for other requests
//      .and()
//      .formLogin().loginPage("/login").defaultSuccessUrl("/customers/list")  // Redirect to home after login
//      .permitAll()
//      .and()
//      .logout().permitAll();
//}

