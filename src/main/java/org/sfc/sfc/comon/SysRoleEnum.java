package org.sfc.sfc.comon;

import lombok.Getter;

@Getter
public enum SysRoleEnum {
    ADMIN("ADMIN", "管理员"),
    EDITOR("EDITOR", "作者"),
    PENDING_1("PENDING_1", "一审员"),
    PENDING_2("PENDING_2", "二审员"),
    PENDING_3("PENDING_3", "三审员");

    private final String code;
    private final String desc;

    SysRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}