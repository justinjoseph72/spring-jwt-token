package com.yoti.app.oauthdemojwt.controllers;

import com.yoti.app.oauthdemojwt.constants.ApiUrlConstants;
import com.yoti.app.oauthdemojwt.domain.RandomCity;
import com.yoti.app.oauthdemojwt.domain.User;
import com.yoti.app.oauthdemojwt.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(ApiUrlConstants.API_BASE_URL)
@Slf4j
public class UserController {


    @Autowired
    private GenericService userService;

    @RequestMapping(value ="/cities")
    @PreAuthorize("hasAuthority('ADMIN_USER') or hasAuthority('STANDARD_USER')")
    public List<RandomCity> getUser(){
        return userService.findAllRandomCities();
    }

    @RequestMapping(value ="/users", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public List<User> getUsers(){
        return userService.findAllUsers();
    }

    @GetMapping(path = "/test")
    public String test(@AuthenticationPrincipal Principal principal){
        return principal.getName() + " You are welcome";
    }
}
