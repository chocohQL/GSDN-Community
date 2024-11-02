package com.gdut.www.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gdut.www.domain.dto.AuthForm;
import com.gdut.www.domain.dto.UserForm;
import com.gdut.www.domain.entity.User;
import com.gdut.www.domain.model.Response;
import com.gdut.www.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author chocoh
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response register(@RequestBody @Validated AuthForm authForm) {
        return Response.success(userService.register(authForm.getUsername(), authForm.getPassword()));
    }

    @PostMapping("/forgetPassword")
    public Response forgetPassword(@RequestBody @Validated AuthForm authForm) {
        return Response.success(userService.forgetPassword(authForm.getUsername(), authForm.getPassword()));
    }

    @PostMapping("/login")
    public Response login(@RequestBody @Validated AuthForm authForm) {
        return Response.success(userService.login(authForm.getUsername(), authForm.getPassword()));
    }

    @GetMapping("/isLogin")
    public Response isLogin() {
        return Response.success(StpUtil.isLogin());
    }

    @PostMapping("/logout")
    public Response logout() {
        StpUtil.logout();
        return Response.success();
    }

    @PostMapping("/modify/userInfo")
    public Response modifyUserInfo(@RequestBody @Validated UserForm user) {
        return Response.success(userService.modifyUserInfo(user));
    }

    @PostMapping("/modify/password")
    public Response modifyPassword(@RequestBody @Validated UserForm user) {
        return Response.success(userService.modifyPassword(user));
    }

    @GetMapping("/me")
    public Response me() {
        return Response.success(userService.getUserInfo(userService.me()));
    }

    @GetMapping("/{id}")
    public Response getUserById(@PathVariable String id) {
        return Response.success(userService.getUserInfo(userService.getById(id)));
    }

    @GetMapping("/get")
    public Response getUser(@RequestParam("username") String username) {
        return Response.success(userService.getUserInfo(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username))));
    }
}