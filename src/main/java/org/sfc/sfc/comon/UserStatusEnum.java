package org.sfc.sfc.comon;


import lombok.Getter;

@Getter
public enum UserStatusEnum {
    NORMAL("NORMAL", "正常"),
    DISABLED("DISABLED", "禁用");

    private final String code;
    private final String desc;

    UserStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}