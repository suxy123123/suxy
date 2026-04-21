package org.sfc.sfc.annotation;


import org.sfc.sfc.comon.SysRoleEnum;

import java.lang.annotation.*;

/**
 * 登录拦截核心注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    SysRoleEnum[] value(); // 允许的角色code
}