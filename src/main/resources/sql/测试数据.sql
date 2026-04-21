-- ============================================
-- SFC 稿件管理系统 - 测试数据
-- ============================================

-- 1. 插入测试用户（密码都是 123456，已使用MD5加密）
-- MD5("123456") = e10adc3949ba59abbe56e057f20f883e

INSERT INTO t_sys_user (username, password, real_name, role_code, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'ADMIN', 'NORMAL'),
('zuozhe', 'e10adc3949ba59abbe56e057f20f883e', '作者', 'EDITOR', 'NORMAL'),
('yishenyuan', 'e10adc3949ba59abbe56e057f20f883e', '一审员', 'PENDING_1', 'NORMAL'),
('ershenyuan', 'e10adc3949ba59abbe56e057f20f883e', '二审员', 'PENDING_2', 'NORMAL');

-- 草稿状态的稿件（作者：zuozhe）
INSERT INTO t_manuscripts (title, content, status, create_time, update_time) VALUES
('人工智能在医疗领域的应用研究', 
 '随着人工智能技术的快速发展，其在医疗领域的应用越来越广泛。本文探讨了AI在疾病诊断、治疗方案推荐、药物研发等方面的应用现状和未来发展趋势。\n\n一、引言\n人工智能作为第四次工业革命的核心技术之一，正在深刻改变着各行各业。医疗行业作为关乎人类健康的重要领域，也迎来了AI技术的深度赋能。\n\n二、AI在疾病诊断中的应用\n1. 影像诊断：通过深度学习算法，AI可以辅助医生进行X光、CT、MRI等医学影像的分析，提高诊断准确率。\n2. 病理分析：AI系统能够快速分析病理切片，识别癌细胞等异常组织。\n\n三、未来展望\n随着技术的不断进步，AI将在个性化医疗、精准治疗等方面发挥更大作用。',
 'DRAFT', NOW(), NOW()),

('基于大数据的城市交通优化方案', 
 '城市交通拥堵是现代社会面临的重大问题之一。本文提出了一种基于大数据分析的城市交通优化方案，通过实时数据采集和智能算法，实现交通流量的动态调控。\n\n一、问题背景\n随着城市化进程加快，机动车数量激增，交通拥堵问题日益严重。传统的交通管理方式已难以满足现代城市的需求。\n\n二、解决方案\n1. 数据采集：通过摄像头、传感器等设备实时采集交通数据\n2. 数据分析：利用大数据技术分析交通流量规律\n3. 智能调控：根据分析结果动态调整信号灯时长\n\n三、实施效果\n经过试点运行，该方案使高峰期通行效率提升了30%以上。',
 'DRAFT', NOW(), NOW());

-- 待一审状态的稿件（作者：zuozhe）
INSERT INTO t_manuscripts (title, content, status, create_time, update_time) VALUES
('新能源汽车电池技术发展综述', 
 '新能源汽车的快速发展离不开电池技术的突破。本文系统梳理了当前主流的新能源汽车电池技术，包括锂离子电池、固态电池、氢燃料电池等，并分析了各自的优势和挑战。\n\n一、锂离子电池\n目前应用最广泛的技术，具有能量密度高、循环寿命长等优点，但存在安全隐患和成本较高的问题。\n\n二、固态电池\n被认为是下一代电池技术，具有更高的安全性和能量密度，但目前量产技术尚不成熟。\n\n三、氢燃料电池\n零排放、加氢速度快，但基础设施建设成本高，制约了其大规模应用。',
 'PENDING_1', NOW(), NOW()),

('5G网络对物联网发展的推动作用', 
 '5G网络的商用部署为物联网发展带来了新的机遇。本文分析了5G技术在物联网场景中的应用优势，包括低延迟、大连接、高带宽等特点如何满足不同行业的需求。\n\n一、5G技术特点\n1. 超低延迟：毫秒级响应，适用于工业控制等场景\n2. 海量连接：每平方公里可连接百万级设备\n3. 高速率：峰值速率可达10Gbps\n\n二、应用场景\n1. 智能制造：实现设备互联互通\n2. 智慧城市：支撑各类传感器数据采集\n3. 远程医疗：支持高清视频传输和远程手术',
 'PENDING_1', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 待二审状态的稿件
INSERT INTO t_manuscripts (title, content, status, create_time, update_time) VALUES
('区块链技术在供应链管理中的应用', 
 '区块链技术以其去中心化、不可篡改的特性，在供应链管理中展现出巨大潜力。本文探讨了区块链如何提升供应链透明度、追溯性和效率。\n\n一、传统供应链的痛点\n1. 信息不对称：各环节信息孤岛现象严重\n2. 追溯困难：产品来源和质量难以追踪\n3. 信任成本高：需要大量中间环节验证\n\n二、区块链解决方案\n1. 建立分布式账本，实现信息共享\n2. 记录完整的产品流转轨迹\n3. 智能合约自动执行交易\n\n三、案例分析\n某跨国食品企业采用区块链技术后，产品追溯时间从7天缩短到2秒。',
 'PENDING_2', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 已退回状态的稿件（需要修改后重提）
INSERT INTO t_manuscripts (title, content, status, reject_reason, last_review_stage, create_time, update_time) VALUES
('云计算安全架构设计', 
 '云计算为企业提供了灵活的计算资源，但安全问题也随之而来。本文讨论了云环境下的安全挑战和应对策略。\n\n一、安全挑战\n1. 数据隐私保护\n2. 访问控制\n3. 合规性要求\n\n二、安全措施\n建议采用多层次防护体系...',
 'REJECTED', 
 '文章内容过于简略，缺乏具体的技术实现细节和案例分析。建议补充：1.具体的安全架构设计方案 2.实际的安全事件案例 3.量化的安全指标。请完善后重新提交。',
 'PENDING_1',
 DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 已完成状态的稿件
INSERT INTO t_manuscripts (title, content, status, create_time, update_time) VALUES
('数字化转型对企业竞争力的影响研究', 
 '在数字经济时代，数字化转型已成为企业提升竞争力的关键路径。本文通过多个行业案例，分析了数字化转型对企业运营模式、客户体验、创新能力等方面的深远影响。\n\n一、研究背景\n全球范围内，越来越多的企业意识到数字化转型的重要性。据调研显示，成功实施数字化转型的企业，其营收增长率平均高出同行23%。\n\n二、转型路径\n1. 战略层面：制定清晰的数字化愿景\n2. 组织层面：建立敏捷的组织架构\n3. 技术层面：构建统一的技术平台\n4. 文化层面：培育数字化思维\n\n三、成功案例\n某传统制造企业通过数字化转型，实现了生产效率提升40%，客户满意度提升35%的显著成效。\n\n四、结论与建议\n数字化转型是一个系统工程，需要顶层设计和持续投入。企业应该根据自身情况，选择合适的转型路径和节奏。',
 'COMPLETED', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 3. 插入审核记录（为部分稿件添加流转历史）

-- 稿件3的审核记录（待一审）
INSERT INTO t_review_records (manuscript_id, operation, from_status, to_status, comment, operate_time) VALUES
(3, 'CREATE', '', 'DRAFT', '创建稿件', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 'SUBMIT', 'DRAFT', 'PENDING_1', '提交一审', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 稿件4的审核记录（待一审）
INSERT INTO t_review_records (manuscript_id, operation, from_status, to_status, comment, operate_time) VALUES
(4, 'CREATE', '', 'DRAFT', '创建稿件', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 'SUBMIT', 'DRAFT', 'PENDING_1', '提交一审', DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 稿件5的审核记录（待二审）
INSERT INTO t_review_records (manuscript_id, operation, from_status, to_status, comment, operate_time) VALUES
(5, 'CREATE', '', 'DRAFT', '创建稿件', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(5, 'SUBMIT', 'DRAFT', 'PENDING_1', '提交一审', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(5, 'APPROVE', 'PENDING_1', 'PENDING_2', '一审通过，内容质量较好', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(5, 'APPROVE', 'PENDING_2', 'PENDING_3', '二审通过', DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 稿件6的审核记录（已退回）
INSERT INTO t_review_records (manuscript_id, operation, from_status, to_status, comment, operate_time) VALUES
(6, 'CREATE', '', 'DRAFT', '创建稿件', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(6, 'SUBMIT', 'DRAFT', 'PENDING_1', '提交一审', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(6, 'REJECT', 'PENDING_1', 'REJECTED', '内容不够详细，需要补充技术细节', DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 稿件7的审核记录（已完成）
INSERT INTO t_review_records (manuscript_id, operation, from_status, to_status, comment, operate_time) VALUES
(7, 'CREATE', '', 'DRAFT', '创建稿件', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(7, 'SUBMIT', 'DRAFT', 'PENDING_1', '提交一审', DATE_SUB(NOW(), INTERVAL 9 DAY)),
(7, 'APPROVE', 'PENDING_1', 'PENDING_2', '一审通过', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(7, 'APPROVE', 'PENDING_2', 'PENDING_3', '二审通过', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(7, 'APPROVE', 'PENDING_3', 'COMPLETED', '终审通过，予以发表', DATE_SUB(NOW(), INTERVAL 5 DAY));

-- ============================================
-- 测试账号说明：
-- ============================================
-- 用户名: admin        密码: 123456（MD5加密） 角色: 管理员
-- 用户名: zuozhe       密码: 123456（MD5加密） 角色: 作者（EDITOR）
-- 用户名: yishenyuan   密码: 123456（MD5加密） 角色: 一审员（PENDING_1）
-- 用户名: ershenyuan   密码: 123456（MD5加密） 角色: 二审员（PENDING_2）
--
-- 所有账号的密码都是：123456
-- MD5加密值：e10adc3949ba59abbe56e057f20f883e
--
-- 稿件状态分布：
-- ID 1-2: DRAFT（草稿）- 作者zuozhe创建
-- ID 3-4: PENDING_1（待一审）- 作者zuozhe创建
-- ID 5: PENDING_2（待二审）
-- ID 6: REJECTED（已退回）- 需要作者修改后重提
-- ID 7: COMPLETED（已完成）
-- ============================================
