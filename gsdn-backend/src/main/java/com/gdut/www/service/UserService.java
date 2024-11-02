package com.gdut.www.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gdut.www.domain.dto.UserForm;
import com.gdut.www.domain.entity.User;
import com.gdut.www.domain.vo.UserInfo;

/**
 * @author chocoh
 */
public interface UserService extends IService<User> {
    String login(String username, String password);

    User register(String username, String password);

    User modifyUserInfo(UserForm user);

    User modifyPassword(UserForm user);

    User me();

    User forgetPassword(String username, String password);

    UserInfo getUserInfo(User user);
}
