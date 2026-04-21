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
 * 稿件主表
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
@Getter
@Setter
@TableName("t_manuscripts")
public class Manuscripts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增，稿件唯一ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 稿件标题，页面列表展示使用
     */
    @TableField("title")
    private String title;

    /**
     * 稿件正文内容，支持长文本
     */
    @TableField("content")
    private String content;

    /**
     * 当前状态：DRAFT=草稿,PENDING_1=待一审,PENDING_2=待二审,PENDING_3=待三审,REJECTED=已退回,COMPLETED=已完成
     */
    @TableField("status")
    private String status;

    /**
     * 审核退回意见，仅退回状态有内容
     */
    @TableField("reject_reason")
    private String rejectReason;

    /**
     * 上一审核阶段，用于退回后重提：PENDING_1/PENDING_2/PENDING_3
     */
    @TableField("last_review_stage")
    private String lastReviewStage;

    /**
     * 记录创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 记录最后更新时间，自动刷新
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
