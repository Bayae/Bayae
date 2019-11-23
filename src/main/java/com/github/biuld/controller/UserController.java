package com.github.biuld.controller;

import com.github.biuld.dto.CredentialAndPassword;
import com.github.biuld.dto.PasswordPair;
import com.github.biuld.model.User;
import com.github.biuld.service.UserService;
import com.github.biuld.util.Encrypt;
import com.github.biuld.util.Result;
import com.github.biuld.util.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Api(tags = "用户管理")
public class UserController {

    private UserService userService;

    @PostMapping("/user/register")
    @ApiOperation("创建用户")
    public Result register(@Validated @RequestBody User user) {

        if (userService.findUserByNameOrEmail(user.getUsername(), user.getEmail()) != null)
            return Result.error(Result.ErrCode.USER_EXISTS);

        return Result.success("ok", userService.create(user));
    }


    @PutMapping("/auth/user/password")
    @ApiOperation("修改密码")
    public Result changePassword(HttpServletRequest request, @RequestBody PasswordPair pair) {
        User user = (User) request.getAttribute("user");

        if (!user.getPassword().equals(Encrypt.SHA512(pair.getOldPassword())))
            return Result.error(Result.ErrCode.PWD_NOT_MATCH);

        user.setPassword(Encrypt.SHA512(pair.getNewPassword()));

        return Result.success("ok", userService.update(user));
    }


    @PostMapping("/user/login")
    @ApiOperation("登录")
    @ApiResponses({@ApiResponse(code = 200, message = "返回token")})
    public Result login(@RequestBody CredentialAndPassword cp) {

        User user = userService.findUserByCredenceAndPassword(cp.getCredential(), cp.getPassword());

        if (user == null)
            return Result.error(Result.ErrCode.USER_NOT_FOUND);

        String token = Token.create(Map.of("userId", user.getId()));
        log.info("{}: {}", user.getUsername(), token);

        return Result.success("ok", token);
    }
}
