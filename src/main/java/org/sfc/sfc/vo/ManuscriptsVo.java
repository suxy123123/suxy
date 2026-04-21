package org.sfc.sfc.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
/**
 * 稿件vo
 */
public class ManuscriptsVo {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * DRAFT草稿/PENDING_1待⼀审/PENDING_2待⼆审/PENDING_3待三审/REJECTED已退回/COMPLETED已完成
     */
    private String status;

    /**
     * 退回原因
     */
    private String rejectReason;

    /**
     * 上一审核阶段
     */
    private String lastReviewStage;

    /**
     * 稿件流转记录
     */
    private Set<ReviewRecordsVo> reviewRecords;

    /**
     * 创建时间
     */
    private java.time.LocalDateTime createTime;

    /**
     * 更新时间
     */
    private java.time.LocalDateTime updateTime;

}
