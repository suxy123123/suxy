package org.sfc.sfc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 审核操作记录表
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
@Getter
@Setter
@TableName("t_review_records")
public class ReviewRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增，记录唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联稿件ID，对应manuscripts.id
     */
    @TableField("manuscript_id")
    private Long manuscriptId;

    /**
     * 操作类型：提交/通过/退回/重提
     */
    @TableField("operation")
    private String operation;

    /**
     * 操作前状态
     */
    @TableField("from_status")
    private String fromStatus;

    /**
     * 操作后状态
     */
    @TableField("to_status")
    private String toStatus;

    /**
     * 操作备注，退回时必填意见
     */
    @TableField("comment")
    private String comment;

    /**
     * 操作发生时间
     */
    @TableField("operate_time")
    private LocalDateTime operateTime;
}
