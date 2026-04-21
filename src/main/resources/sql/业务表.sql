
-- 稿件表
CREATE TABLE t_manuscripts (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               title VARCHAR(255) NOT NULL COMMENT '标题',
                               content TEXT COMMENT '内容',
                               status VARCHAR(50) NOT NULL COMMENT 'DRAFT/PENDING_1/PENDING_2/PENDING_3/REJECTED/COMPLETED',
                               reject_reason TEXT COMMENT '退回原因',
                               last_review_stage VARCHAR(50) COMMENT '上一审核阶段',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                               update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审核记录表
CREATE TABLE t_review_records (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  manuscript_id BIGINT NOT NULL,
                                  operation VARCHAR(50) NOT NULL COMMENT '提交/通过/退回/重提',
                                  from_status VARCHAR(50),
                                  to_status VARCHAR(50),
                                  comment TEXT,
                                  operate_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  INDEX idx_manuscript_id (manuscript_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;