package org.sfc.sfc.vo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
/**
 * 稿件流转记录vo
 */
public class ReviewRecordsVo {
    /**
     * 提交/通过/退回/重提
     */
    private String operation;

    /**
     * 上一环节状态
     */
    private String fromStatus;

    /**
     * 下一环节状态
     */
    private String toStatus;

    /**
     * 备注
     */
    private String comment;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;
}
