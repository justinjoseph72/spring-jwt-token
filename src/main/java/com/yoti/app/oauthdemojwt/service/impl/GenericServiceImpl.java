package com.yoti.app.oauthdemojwt.service.impl;

import com.yoti.app.oauthdemojwt.domain.RandomCity;
import com.yoti.app.oauthdemojwt.domain.User;
import com.yoti.app.oauthdemojwt.repos.RandomCityRepository;
import com.yoti.app.oauthdemojwt.repos.RoleRepository;
import com.yoti.app.oauthdemojwt.repos.UserRepository;
import com.yoti.app.oauthdemojwt.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RandomCityRepository randomCityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>)userRepository.findAll();
    }

    @Override
    public List<RandomCity> findAllRandomCities() {
        return (List<RandomCity>)randomCityRepository.findAll();
    }
}
