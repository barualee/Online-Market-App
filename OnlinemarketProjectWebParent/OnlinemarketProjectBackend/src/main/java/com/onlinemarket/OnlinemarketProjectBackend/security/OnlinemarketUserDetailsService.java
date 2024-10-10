package com.onlinemarket.OnlinemarketProjectBackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.onlinemarket.OnlinemarketProjectBackend.user.UserRepository;
import com.onlinemarket.OnlinemarketProjectCommon.entity.User;

public class OnlinemarketUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		
		if(user != null) {
			return new OnlinemarketUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email: "+email);
	}

}
