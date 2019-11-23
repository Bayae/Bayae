package com.github.biuld.mapper;

import com.github.biuld.model.Repo;
import com.github.biuld.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RepoMapper extends BaseMapper<Repo> {
}