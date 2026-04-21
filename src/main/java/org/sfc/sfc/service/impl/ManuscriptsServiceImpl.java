package org.sfc.sfc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.sfc.sfc.comon.ManuscriptStatusEnum;
import org.sfc.sfc.comon.OperationTypeEnum;
import org.sfc.sfc.comon.Result;
import org.sfc.sfc.entity.Manuscripts;
import org.sfc.sfc.entity.ReviewRecords;
import org.sfc.sfc.mapper.ManuscriptsMapper;
import org.sfc.sfc.service.ManuscriptsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sfc.sfc.service.ReviewRecordsService;
import org.sfc.sfc.vo.ManuscriptsVo;
import org.sfc.sfc.vo.ReviewRecordsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 稿件主表 服务实现类
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
@Service
public class ManuscriptsServiceImpl extends ServiceImpl<ManuscriptsMapper, Manuscripts> implements ManuscriptsService {

    @Resource
    private ReviewRecordsService reviewRecordsService;

    @Override
    public Result<?> createManuscripts(ManuscriptsVo manuscriptsVo) {
        Manuscripts manuscripts = new Manuscripts();
        BeanUtils.copyProperties(manuscriptsVo,manuscripts);
        manuscripts.setStatus(ManuscriptStatusEnum.DRAFT.getCode());
        this.save(manuscripts);
        return Result.ok("创建稿件成功！");
    }

    @Override
    public Result<?> editManuscripts(ManuscriptsVo manuscriptsVo) {
        Manuscripts manuscripts = new Manuscripts();
        BeanUtils.copyProperties(manuscriptsVo,manuscripts);
        this.updateById(manuscripts);
        return Result.ok("修改稿件成功！");
    }

    @Override
    public Result<?> pageList(Integer pageNum, Integer pageSize, ManuscriptsVo manuscriptsVo) {
        Page<Manuscripts> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Manuscripts> queryWrapper = new LambdaQueryWrapper<Manuscripts>()
                .like(StringUtils.isNotBlank(manuscriptsVo.getTitle()),
                        Manuscripts::getTitle, manuscriptsVo.getTitle()) //标题
                .like(StringUtils.isNotBlank(manuscriptsVo.getContent()),
                        Manuscripts::getContent, manuscriptsVo.getContent()) //内容
                .eq(StringUtils.isNotBlank(manuscriptsVo.getStatus()),
                        Manuscripts::getStatus, manuscriptsVo.getStatus()) //状态
                .like(StringUtils.isNotBlank(manuscriptsVo.getRejectReason()),
                        Manuscripts::getRejectReason, manuscriptsVo.getRejectReason()) //退回理由
                .eq(StringUtils.isNotBlank(manuscriptsVo.getLastReviewStage()),
                        Manuscripts::getLastReviewStage, manuscriptsVo.getLastReviewStage()) //上一审核阶段
                .eq(manuscriptsVo.getId() != null,
                        Manuscripts::getId, manuscriptsVo.getId())
                .orderByDesc(Manuscripts::getCreateTime);
        return Result.ok(this.page(page, queryWrapper));
    }

    @Override
    public Result<?> getDetailById(Long id) {
        ManuscriptsVo manuscriptsVo = new ManuscriptsVo();

        //1查询稿件基础信息
        Manuscripts manuscripts = this.getById(id);
        if(manuscripts==null){
            log.error(String.format("本次查询稿件信息不存在，id【%s】",id));
            throw new RuntimeException("本次查询稿件信息不存在");
        }
        BeanUtils.copyProperties(manuscripts,manuscriptsVo);

        //2.查询稿件对应的流转信息,按创建时间倒排，最近的流程节点在最前
        List<ReviewRecords> reviewRecords = reviewRecordsService.lambdaQuery()
                .eq(ReviewRecords::getManuscriptId, id)
                .orderByDesc(ReviewRecords::getOperateTime)
                .list();
        Set<ReviewRecordsVo> reviewRecordsVos = reviewRecords.stream()
                .map(this::convertToReviewVo)
                .collect(Collectors.toSet());
        manuscriptsVo.setReviewRecords(reviewRecordsVos);
        return Result.ok(manuscriptsVo);
    }

    /**
     * 处理流转记录向vo转换
     * @param entity
     * @return
     */
    private ReviewRecordsVo convertToReviewVo(ReviewRecords entity) {
        ReviewRecordsVo vo = new ReviewRecordsVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    @Override
    public Result<?> submitPending1(Long id) {
        //1将稿件基础状态改为PENDING_1
        Manuscripts manuscripts = this.getById(id);
        if(manuscripts==null){
            log.error(String.format("稿件信息不存在，id【%s】",id));
            throw new RuntimeException("本次提交稿件信息不存在，请验证后再试");
        }
        manuscripts.setStatus(ManuscriptStatusEnum.PENDING_1.getCode());
        this.updateById(manuscripts);

        //新增流转记录
        ReviewRecords reviewRecords = new ReviewRecords();
        reviewRecords.setManuscriptId(id);
        reviewRecords.setOperation(OperationTypeEnum.SUBMIT.getCode());
        reviewRecords.setFromStatus(ManuscriptStatusEnum.DRAFT.getCode());
        reviewRecords.setToStatus(ManuscriptStatusEnum.PENDING_1.getCode());
        reviewRecordsService.save(reviewRecords);
        return Result.ok("提交成功");
    }

    @Override
    public Result<?> approve(Long id) {

        //1.确定后续环节节点
        Manuscripts manuscripts = this.getById(id);
        //通过当前状态来决定审批通过的环节
        String currentStatus = manuscripts.getStatus();
        ManuscriptStatusEnum currentEnum = ManuscriptStatusEnum.getByCode(currentStatus);
        String nextStatus = switch (currentEnum) {
            case PENDING_1 -> ManuscriptStatusEnum.PENDING_2.getCode();
            case PENDING_2 -> ManuscriptStatusEnum.PENDING_3.getCode();
            case PENDING_3 -> ManuscriptStatusEnum.COMPLETED.getCode();
            default -> throw new RuntimeException("当前状态不可审核通过");
        };
        manuscripts.setStatus(nextStatus);
        this.updateById(manuscripts);

        //2.新增流程记录
        ReviewRecords reviewRecords = new ReviewRecords();
        reviewRecords.setManuscriptId(id);
        reviewRecords.setOperation(OperationTypeEnum.APPROVE.getCode());
        reviewRecords.setFromStatus(currentStatus);
        reviewRecords.setToStatus(nextStatus);
        reviewRecordsService.save(reviewRecords);
        return Result.ok("审批成功");
    }

    @Override
    public Result<?> reject(ManuscriptsVo manuscriptsVo) {
        //1.校验审核退回意见，如果为空，不允许退回
        if(StringUtils.isBlank(manuscriptsVo.getRejectReason())){
            log.error(String.format("退回时审核意见不可为空，id【%s】",manuscriptsVo.getId()));
            throw new RuntimeException("退回时审核意见不可为空");
        }
        /**
         * 2.分别对几个关键字段赋值
         * 上一审核阶段->填入更新前的状态，状态->退回,退回理由->填入页面返回的内容
         */
        Manuscripts manuscripts = this.getById(manuscriptsVo.getId());
        String currStatus = manuscripts.getStatus();
        manuscripts.setStatus(ManuscriptStatusEnum.REJECTED.getCode());
        manuscripts.setRejectReason(manuscriptsVo.getRejectReason());
        manuscriptsVo.setLastReviewStage(currStatus);
        this.updateById(manuscripts);

        //3.新增流转记录
        ReviewRecords record = new ReviewRecords();
        record.setManuscriptId(manuscriptsVo.getId());
        record.setOperation(OperationTypeEnum.REJECT.getCode());
        record.setFromStatus(currStatus);
        record.setToStatus(ManuscriptStatusEnum.REJECTED.getCode());
        record.setComment(manuscriptsVo.getRejectReason());
        reviewRecordsService.save(record);
        return Result.ok("退回成功");
    }

    @Override
    public Result<?> resubmit(Long id) {
        //1.使用稿件的上一审核阶段设置当前状态，并入库
        Manuscripts manuscripts = this.getById(id);
        String lastStage = manuscripts.getLastReviewStage();
        manuscripts.setStatus(lastStage);
        this.updateById(manuscripts);

        //2.新增流转记录
        ReviewRecords record = new ReviewRecords();
        record.setManuscriptId(id);
        record.setOperation(OperationTypeEnum.RESUBMIT.getCode());
        record.setFromStatus(ManuscriptStatusEnum.REJECTED.getCode());
        record.setToStatus(lastStage);
        reviewRecordsService.save(record);
        return Result.ok("重提成功");
    }
}
