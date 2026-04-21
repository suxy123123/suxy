package org.sfc.sfc.controller;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.sfc.sfc.comon.Result;
import org.sfc.sfc.entity.SysUser;
import org.sfc.sfc.service.SysUserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
@CrossOrigin
public class AccountController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody SysUser vo, HttpSession session) {
        SysUser user = sysUserService.login(vo.getUsername(), vo.getPassword());
        if (user == null) {
            return Result.error(401, "账号或密码错误");
        }
        session.setAttribute("loginUser", user);
        return Result.ok(user);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody SysUser vo) {
        sysUserService.register(vo);
        return Result.ok("注册成功");
    }
}