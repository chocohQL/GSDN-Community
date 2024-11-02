package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.www.common.ResponseMsg;
import com.gdut.www.domain.dto.UserForm;
import com.gdut.www.domain.entity.User;
import com.gdut.www.domain.vo.UserInfo;
import com.gdut.www.exception.GlobalException;
import com.gdut.www.mapper.UserMapper;
import com.gdut.www.service.FileService;
import com.gdut.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chocoh
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService fileService;

    @Override
    public String login(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new GlobalException(ResponseMsg.USERNAME_NOT_FOUNT);
        }
        if (DigestUtil.bcryptCheck(password, user.getPassword())) {
            StpUtil.login(user.getId());
            return StpUtil.getTokenInfo().getTokenValue();
        } else {
            throw new GlobalException(ResponseMsg.PASSWORD_ERROR);
        }
    }

    @Override
    public User register(String username, String password) {
        if (getUserByUsername(username) != null) {
            throw new GlobalException(ResponseMsg.USERNAME_EXIST);
        }
        User user = User.builder()
                .username(username)
                .password(DigestUtil.bcrypt(password))
                .build();
        userMapper.insert(user);
        return user;
    }

    @Override
    public User modifyUserInfo(UserForm user) {
        User me = me();
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            fileService.deleteImg(me.getAvatar());
            me.setAvatar(user.getAvatar());
        }
        if (StringUtils.isNotEmpty(user.getUsername()) && !user.getUsername().equals(me.getUsername())) {
            if (getUserByUsername(user.getUsername()) == null) {
                me.setUsername(user.getUsername());
            } else {
                throw new GlobalException(ResponseMsg.USERNAME_EXIST);
            }
        }
        if (StringUtils.isNotBlank(user.getIntro())) {
            me.setIntro(user.getIntro());
        }
        userMapper.updateById(me);
        return me;
    }

    @Override
    public User modifyPassword(UserForm user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            User me = me();
            me.setPassword(DigestUtil.bcrypt(user.getPassword()));
            userMapper.updateById(me);
            return me;
        } else {
            throw new GlobalException(ResponseMsg.PARAMETER_EMPTY);
        }
    }

    @Override
    public User forgetPassword(String username, String password) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new GlobalException(ResponseMsg.USERNAME_NOT_FOUNT);
        }
        user.setPassword(DigestUtil.bcrypt(password));
        userMapper.updateById(user);
        return user;
    }

    @Override
    public UserInfo getUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        return userInfo;
    }

    @Override
    public User me() {
        return userMapper.selectById(StpUtil.getLoginIdAsLong());
    }

    private User getUserByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }
}
