package org.sfc.sfc.service;

import org.sfc.sfc.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
public interface SysUserService extends IService<SysUser> {
    SysUser login(String username, String password);
    void register(SysUser user);
}
