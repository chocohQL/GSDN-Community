package com.gdut.www.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdut.www.domain.entity.UserDetails;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chocoh
 */
@Mapper
public interface UserDetailsMapper extends BaseMapper<UserDetails> {
}
