
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
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username)
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
