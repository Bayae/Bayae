package com.github.biuld.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Data
public class Repo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @Column(name = "repo_key")
    @Size(max = 50, message = "库名不能超过50个字符")
    private String repoKey;

    @Column(name = "repo_desc")
    private String repoDesc;

    @Column(name = "repo_value")
    private String repoValue;

    @ApiModelProperty(hidden = true)
    private Integer likes;
}