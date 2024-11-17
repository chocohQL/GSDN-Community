package com.gdut.www.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.www.domain.dto.UserReq;
import com.gdut.www.domain.entity.User;
import com.gdut.www.domain.dto.UserResp;

import java.util.List;

/**
 * @author chocoh
 */
public interface UserService extends IService<User> {
    String login(String username, String password);

    User register(String username, String password);

    User modifyUserInfo(UserReq user);

    User modifyPassword(UserReq user);

    User me();

    User forgetPassword(String username, String password);

    UserResp getUserInfo(User user);

    void follow(Long userId);

    boolean isFollow(Long userId);

    List<UserResp> fans(Long userId);

    List<UserResp> follows(Long userId);

    List<UserResp> search(String key);
}
