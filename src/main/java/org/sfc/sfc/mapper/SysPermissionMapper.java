package org.sfc.sfc.mapper;

import org.sfc.sfc.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    @Select("SELECT p.permission_key " +
            "FROM t_sys_permission p " +
            "JOIN t_sys_role_permission rp ON p.permission_key = rp.permission_key " +
            "WHERE rp.role_code = #{roleCode}")
    List<String> selectPermissionKeysByRoleCode(String roleCode);
}
