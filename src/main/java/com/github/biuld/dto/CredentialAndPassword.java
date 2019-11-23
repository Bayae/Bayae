package com.github.biuld.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CredentialAndPassword {

    @ApiModelProperty("用户名或邮箱")
    private String credential;

    @ApiModelProperty("密码")
    private String password;
}
