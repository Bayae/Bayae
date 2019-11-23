package com.github.biuld.service;

import com.github.biuld.mapper.UserMapper;
import com.github.biuld.model.User;
import com.github.biuld.util.Encrypt;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private UserMapper userMapper;

    public User findUserByNameOrEmail(String username, String email) {
        return Optional.ofNullable(this.findUserByName(username))
                .orElse(this.findUserByEmail(email));
    }


    private User findUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
    }

    private User findUserByName(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(user);
    }

    public User findUserByCredenceAndPassword(String credence, String password) {
        return Optional.ofNullable(this.findUserByNameAndPwd(credence, password))
                .orElse(this.findUserByEmailAndPwd(credence, password));
    }

    private User findUserByEmailAndPwd(String email, String password) {
        return Optional.of(new User())
                .map(user -> {
                    user.setEmail(email);
                    user.setPassword(Encrypt.SHA512(password));
                    return user;
                }).map(userMapper::selectOne)
                .orElse(null);
    }

    private User findUserByNameAndPwd(String name, String password) {
        return Optional.of(new User())
                .map(user -> {
                    user.setUsername(name);
                    user.setPassword(Encrypt.SHA512(password));
                    return user;
                }).map(userMapper::selectOne)
                .orElse(null);
    }

    public int create(User user) {
        user.setPassword(Encrypt.SHA512(user.getPassword()));
        return userMapper.insertSelective(user);
    }

    public int update(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
