package com.github.biuld.controller;

import com.github.biuld.model.Repo;
import com.github.biuld.service.RepoService;
import com.github.biuld.util.Page;
import com.github.biuld.util.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "仓库管理")
public class RepoController {

    @Autowired
    private RepoService repoService;

    @PostMapping("/auth/repo")
    public Result<Integer> create(@RequestBody Repo repo) {
        return Result.success("ok", repoService.create(repo));
    }

    @GetMapping("/repo/all")
    public Result<Page<Repo>> readList(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success("ok", repoService.readList(pageNum, pageSize));
    }

    @PutMapping("/repo/likes")
    public Result<Integer> likes(@RequestParam Integer id) {
        return Result.success("ok", repoService.like(id));
    }
}
