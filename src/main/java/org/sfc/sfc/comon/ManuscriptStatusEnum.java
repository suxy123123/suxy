package org.sfc.sfc.comon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManuscriptStatusEnum {

    DRAFT("DRAFT", "草稿"),
    PENDING_1("PENDING_1", "待一审"),
    PENDING_2("PENDING_2", "待二审"),
    PENDING_3("PENDING_3", "待三审"),
    REJECTED("REJECTED", "已退回"),
    COMPLETED("COMPLETED", "已完成");

    private final String code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static ManuscriptStatusEnum getByCode(String code) {
        for (ManuscriptStatusEnum enums : ManuscriptStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }
}