package com.onlinemarket.OnlinemarketProjectFrontend.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Customer;

public class CustomerUserDetails implements UserDetails {
    private Customer customer;

    public CustomerUserDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}

    public String getFullName() {
        return customer.getFullName();
    }
}
