package org.sfc.sfc.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sfc.sfc.comon.Result;
import org.sfc.sfc.entity.SysUser;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 登录核心拦截器
 * 用于处理登录校验，登录成功后将用户信息放在session中
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SysUser user = (SysUser) request.getSession().getAttribute("loginUser");
        if (user == null) {
            returnError(response, Result.error(401, "请先登录"));
            return false;
        }
        return true;
    }

    private void returnError(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getOutputStream(), result);
    }
}