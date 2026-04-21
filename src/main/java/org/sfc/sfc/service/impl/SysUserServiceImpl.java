package org.sfc.sfc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.sfc.sfc.comon.SysRoleEnum;
import org.sfc.sfc.comon.UserStatusEnum;
import org.sfc.sfc.entity.SysUser;
import org.sfc.sfc.mapper.SysUserMapper;
import org.sfc.sfc.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public SysUser login(String username, String password) {
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username)
                .eq(SysUser::getPassword, pwd);
        return getOne(wrapper);
    }

    @Override
    public void register(SysUser user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setRoleCode(SysRoleEnum.EDITOR.getCode());
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        save(user);
    }
}
