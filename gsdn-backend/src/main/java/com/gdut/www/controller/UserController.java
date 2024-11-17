package com.gdut.www.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gdut.www.domain.dto.AuthReq;
import com.gdut.www.domain.dto.UserReq;
import com.gdut.www.domain.entity.User;
import com.gdut.www.domain.model.Response;
import com.gdut.www.service.UserService;
import com.gdut.www.utils.PageUtil;
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
    public Response register(@RequestBody @Validated AuthReq authReq) {
        return Response.success(userService.register(authReq.getUsername(), authReq.getPassword()));
    }

    @PostMapping("/forgetPassword")
    public Response forgetPassword(@RequestBody @Validated AuthReq authReq) {
        return Response.success(userService.forgetPassword(authReq.getUsername(), authReq.getPassword()));
    }

    @PostMapping("/login")
    public Response login(@RequestBody @Validated AuthReq authReq) {
        return Response.success(userService.login(authReq.getUsername(), authReq.getPassword()));
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
    public Response modifyUserInfo(@RequestBody @Validated UserReq user) {
        return Response.success(userService.modifyUserInfo(user));
    }

    @PostMapping("/modify/password")
    public Response modifyPassword(@RequestBody @Validated UserReq user) {
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
    @PostMapping("/follow")
    public Response follow(@RequestParam("userId") Long userId) {
        userService.follow(userId);
        return Response.success();
    }

    @GetMapping("/isFollow")
    public Response isFollow(@RequestParam("userId") Long userId) {
        return Response.success(userService.isFollow(userId));
    }

    @GetMapping("/fans")
    public Response fans(@RequestParam("userId") Long userId) {
        return Response.success(userService.fans(userId));
    }

    @GetMapping("/follows")
    public Response follows(@RequestParam("userId") Long userId) {
        return Response.success(userService.follows(userId));
    }

    @GetMapping("/search")
    public Response search(@RequestParam("key") String key,
                           @RequestParam("page") Integer page,
                           @RequestParam("pageSize") Integer pageSize) {
        return Response.success(PageUtil.getPageBean(userService.search(key), page, pageSize));
    }
}