package com.github.biuld.config.security;

import com.github.biuld.config.Constants;
import com.github.biuld.mapper.UserMapper;
import com.github.biuld.model.User;
import com.github.biuld.util.Result;
import com.github.biuld.util.Token;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Created by biuld on 2019/8/21.
 */
@AllArgsConstructor()
public class JwtFilter extends OncePerRequestFilter {

    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String token = request.getHeader(Constants.TOKEN_KEY);

        if (StringUtils.isEmpty(token)) {
            print(response, Result.error(Result.ErrCode.JWT_ERRCODE_NULL));
            return;
        }

        Result result = Token.validate(token);

        //token验证不成功
        if (result.getCode() != 200) {
            print(response, result);
            return;
        }

        Claims claims = (Claims) result.getData();

        User user = Optional.ofNullable((Integer) claims.get("userId"))
                .map(userMapper::selectByPrimaryKey)
                .orElse(null);

        if (user == null) {
            print(response, Result.ErrCode.JWT_ERRCODE_BROKEN);
            return;
        }

        //加载用户信息到request，方便后续使用
        request.setAttribute("user", user);

        chain.doFilter(request, response);
    }

    private void print(HttpServletResponse response, Object message) {
        try (PrintWriter writer = response.getWriter();) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", "*");
            writer.write(message.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
