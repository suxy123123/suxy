
-- 用户表
CREATE TABLE t_sys_user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username    VARCHAR(50) NOT NULL COMMENT '账号',
    password    VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    real_name   VARCHAR(50) COMMENT '真实姓名',
    role_code   VARCHAR(50) NOT NULL COMMENT '角色编码：ADMIN/EDITOR/REVIEWER',
    status      VARCHAR(20) DEFAULT 'NORMAL' COMMENT '状态：NORMAL/DISABLED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
-- 权限表
CREATE TABLE t_sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_key VARCHAR(50) NOT NULL COMMENT '权限标识',
    UNIQUE KEY uk_permission_key (permission_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE t_sys_role_permission (
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    permission_key VARCHAR(50) NOT NULL COMMENT '权限标识',
    PRIMARY KEY (role_code, permission_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- ============================================
-- 测试账号说明：
-- ============================================
-- 用户名: admin        密码: 123456（MD5加密） 角色: 管理员
-- 用户名: zuozhe       密码: 123456（MD5加密） 角色: 作者（EDITOR）
-- 用户名: yishenyuan   密码: 123456（MD5加密） 角色: 一审员（PENDING_1）
-- 用户名: ershenyuan   密码: 123456（MD5加密） 角色: 二审员（PENDING_2）
-- 用户名: sanshenyuan  密码: 123456（MD5加密） 角色: 三审员（PENDING_3）
--
-- 所有账号的密码都是：123456
-- MD5加密值：e10adc3949ba59abbe56e057f20f883e

INSERT INTO t_sys_user (username, password, real_name, role_code, status) VALUES
                                                                              ('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'ADMIN', 'NORMAL'),
                                                                              ('zuozhe', 'e10adc3949ba59abbe56e057f20f883e', '作者', 'EDITOR', 'NORMAL'),
                                                                              ('yishenyuan', 'e10adc3949ba59abbe56e057f20f883e', '一审员', 'PENDING_1', 'NORMAL'),
                                                                              ('ershenyuan', 'e10adc3949ba59abbe56e057f20f883e', '二审员', 'PENDING_2', 'NORMAL'),
                                                                              ('sanshenyuan', 'e10adc3949ba59abbe56e057f20f883e', '三审员', 'PENDING_3', 'NORMAL');



-- 初始化权限数据
INSERT INTO t_sys_permission (permission_name, permission_key) VALUES
('查看稿件', 'manuscript:view'),
('创建稿件', 'manuscript:create'),
('编辑稿件', 'manuscript:edit'),
('提交审核', 'manuscript:submit'),
('审核通过', 'manuscript:approve'),
('审核退回', 'manuscript:reject'),
('重新提交', 'manuscript:resubmit');

-- 初始化角色权限关联（管理员拥有全部权限）
INSERT INTO t_sys_role_permission (role_code, permission_key) VALUES
('ADMIN', 'manuscript:view'), ('ADMIN', 'manuscript:create'), ('ADMIN', 'manuscript:edit'),
('ADMIN', 'manuscript:submit'), ('ADMIN', 'manuscript:approve'), ('ADMIN', 'manuscript:reject'),
('ADMIN', 'manuscript:resubmit');

-- 作者权限
INSERT INTO t_sys_role_permission (role_code, permission_key) VALUES
('EDITOR', 'manuscript:view'), ('EDITOR', 'manuscript:create'), ('EDITOR', 'manuscript:edit'),
('EDITOR', 'manuscript:submit'), ('EDITOR', 'manuscript:resubmit');

-- 一审员权限
INSERT INTO t_sys_role_permission (role_code, permission_key) VALUES
('PENDING_1', 'manuscript:view'), ('PENDING_1', 'manuscript:approve'), ('PENDING_1', 'manuscript:reject');

-- 二审员权限
INSERT INTO t_sys_role_permission (role_code, permission_key) VALUES
('PENDING_2', 'manuscript:view'), ('PENDING_2', 'manuscript:approve'), ('PENDING_2', 'manuscript:reject');

-- 三审员权限
INSERT INTO t_sys_role_permission (role_code, permission_key) VALUES
('PENDING_3', 'manuscript:view'), ('PENDING_3', 'manuscript:approve'), ('PENDING_3', 'manuscript:reject');
