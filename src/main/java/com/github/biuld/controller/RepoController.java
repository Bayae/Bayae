package com.github.biuld.controller;

import com.github.biuld.model.Repo;
import com.github.biuld.service.RepoService;
import com.github.biuld.util.Page;
import com.github.biuld.util.Result;
import com.github.biuld.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Api(tags = "仓库管理")
public class RepoController {

    @Autowired
    private RepoService repoService;

    @Value("${spring.servlet.multipart.location}")
    private String base;

    @PostMapping("/repo")
    public Result<Integer> create(@RequestBody Repo repo) {
        return Result.success("ok", repoService.create(repo));
    }

    @PostMapping("/auth/repo/like/{repoId}")
    public Result like(@PathVariable(value = "repoId") Integer repoId) {

        if (repoId <= 0)
            return Result.error(Result.ErrCode.ILLEGAL_ID);

        return Result.success("ok", repoService.like(repoId));
    }

    @PostMapping("/auth/upload")
    public Result<String> upload(@ApiParam(value = "仓库文件") MultipartFile file) {

        String originalFilename = file.getOriginalFilename();    //原始名称

        //新的图片名称
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUIDUtil.getUUID()+ext;

        File newFile = new File(base + newFileName);

        //将内存中的数据写入磁盘
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            return Result.error(Result.ErrCode.UPLOAD_ERROR);
        }

        return Result.success("ok", newFileName);
    }

    @GetMapping("/repo/all")
    public Result<Page<Repo>> readList(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success("ok", repoService.readList(pageNum, pageSize));
    }
}
