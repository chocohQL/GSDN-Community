package com.gdut.www.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.www.common.ResponseMsg;
import com.gdut.www.domain.dto.UserReq;
import com.gdut.www.domain.entity.Follows;
import com.gdut.www.domain.entity.User;
import com.gdut.www.domain.dto.UserResp;
import com.gdut.www.exception.GlobalException;
import com.gdut.www.mapper.FollowsMapper;
import com.gdut.www.mapper.UserMapper;
import com.gdut.www.service.FileService;
import com.gdut.www.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private FollowsMapper followsMapper;
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
    public User modifyUserInfo(UserReq user) {
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
    public User modifyPassword(UserReq user) {
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
    public UserResp getUserInfo(User user) {
        UserResp userResp = UserResp.builder()
                .fans(followsMapper.selectCount(new LambdaQueryWrapper<Follows>()
                        .eq(Follows::getFollowsId, user.getId())))
                .follows(followsMapper.selectCount(new LambdaQueryWrapper<Follows>()
                        .eq(Follows::getUserId, user.getId())))
                .build();
        BeanUtils.copyProperties(user, userResp);
        return userResp;
    }

    @Override
    public User me() {
        return userMapper.selectById(StpUtil.getLoginIdAsLong());
    }

    private User getUserByUsername(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void follow(Long userId) {
        Follows follows = getFollows(userId);
        if (follows == null) {
            followsMapper.insert(Follows.builder()
                    .userId(StpUtil.getLoginIdAsLong())
                    .followsId(userId)
                    .build());
        } else {
            followsMapper.deleteById(follows.getId());
        }
    }

    @Override
    public boolean isFollow(Long userId) {
        return getFollows(userId) != null;
    }

    @Override
    public List<UserResp> fans(Long userId) {
        List<Follows> follows = followsMapper.selectList(new LambdaQueryWrapper<Follows>().eq(Follows::getFollowsId, userId));
        List<Long> userIds = follows.stream().map(Follows::getUserId).collect(Collectors.toList());
        List<UserResp> userRespList = new ArrayList<>();
        userIds.forEach(id -> userRespList.add(getUserInfo(getById(id))));
        return userRespList;
    }

    @Override
    public List<UserResp> follows(Long userId) {
        List<Follows> follows = followsMapper.selectList(new LambdaQueryWrapper<Follows>().eq(Follows::getUserId, userId));
        List<Long> userIds = follows.stream().map(Follows::getFollowsId).collect(Collectors.toList());
        List<UserResp> userRespList = new ArrayList<>();
        userIds.forEach(id -> userRespList.add(getUserInfo(getById(id))));
        return userRespList;
    }

    @Override
    public List<UserResp> search(String key) {
        List<UserResp> userRespList = new ArrayList<>();
        userMapper.selectList(new LambdaQueryWrapper<User>()
                        .like(User::getUsername, key)
                        .or()
                        .like(User::getIntro, key))
                .forEach(user -> userRespList.add(getUserInfo(user)));
        return userRespList;
    }

    private Follows getFollows(Long userId) {
        return followsMapper.selectOne(new LambdaQueryWrapper<Follows>()
                .eq(Follows::getFollowsId, userId)
                .eq(Follows::getUserId, StpUtil.getLoginIdAsLong()));
    }
}
