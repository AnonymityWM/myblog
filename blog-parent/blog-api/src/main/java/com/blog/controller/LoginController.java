package com.blog.controller;

import com.blog.service.LoginService;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){

        return loginService.login(loginParam);
    }

    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }

    @PostMapping("register")
    public Result register(@RequestBody LoginParam loginParam){
        return loginService.register(loginParam);
    }
}