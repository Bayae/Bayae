package com.github.biuld.service;

import com.github.biuld.config.exception.BusinessException;
import com.github.biuld.mapper.RepoMapper;
import com.github.biuld.model.Repo;
import com.github.biuld.util.Page;
import com.github.biuld.util.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RepoService {

    private RepoMapper repoMapper;

    public int create(Repo repo) {

        Repo tester =  new Repo();
        tester.setRepoKey(repo.getRepoKey());

        Optional.ofNullable(repoMapper.select(tester))
                .ifPresent(test -> {
                    throw new BusinessException(Result.ErrCode.KEY_DUPLICATED);
                });

        return repoMapper.insertSelective(repo);
    }

    public int like(Integer repoId) {

        return Optional.ofNullable(repoMapper.selectByPrimaryKey(repoId))
                .map(repo -> {
                    repo.setLikes(repo.getLikes() + 1);
                    return repo;
                }).map(repoMapper::updateByPrimaryKeySelective).orElse(0);
    }

    public Page<Repo> readList(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        Example example = new Example(Repo.class);
        example.orderBy("likes").desc();

        List<Repo> result = repoMapper.selectByExample(example);

        PageInfo<Repo> pageInfo = new PageInfo<>(result);

        return Page.of(pageInfo, result);
    }
}
