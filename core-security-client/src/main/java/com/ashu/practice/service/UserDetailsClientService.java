package com.ashu.practice.service;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ashu.practice.model.UserDetailsImpl;
import com.ashu.practice.utils.CacheConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsClientService implements UserDetailsService {

	private final RestTemplate restTemplate;

	@Cacheable(cacheNames = { CacheConstants.USER_DETAILS_CACHE }, key = "#username")
	@Override
	public UserDetails loadUserByUsername(String username) {
		return getUserDetails(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
	}

	private Optional<UserDetailsImpl> getUserDetails(String username) {
		ResponseEntity<UserDetailsImpl> response = restTemplate.getForEntity("/" + username, UserDetailsImpl.class);
		return Optional.ofNullable(response.getBody());
	}

}