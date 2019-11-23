package com.github.biuld.mapper;

import com.github.biuld.model.User;
import com.github.biuld.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}