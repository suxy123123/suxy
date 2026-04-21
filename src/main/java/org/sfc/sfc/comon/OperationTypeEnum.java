package org.sfc.sfc.comon;

import lombok.Getter;

@Getter
public enum OperationTypeEnum {

    SUBMIT("SUBMIT", "提交"),
    APPROVE("APPROVE", "通过"),
    REJECT("REJECT", "退回"),
    RESUBMIT("RESUBMIT", "重提");

    private final String code;
    private final String desc;

    OperationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}