package org.sfc.sfc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 权限表
 * </p>
 */
@Getter
@Setter
@TableName("t_sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @TableField("permission_name")
    private String permissionName;

    @TableField("permission_key")
    private String permissionKey;
}
