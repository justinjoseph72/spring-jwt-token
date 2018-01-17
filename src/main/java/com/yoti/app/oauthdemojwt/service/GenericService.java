package com.yoti.app.oauthdemojwt.service;

import com.yoti.app.oauthdemojwt.domain.RandomCity;
import com.yoti.app.oauthdemojwt.domain.User;

import java.util.List;

public interface GenericService {

    User findByUsername(String username);

    List<User> findAllUsers();

    List<RandomCity> findAllRandomCities();
}
