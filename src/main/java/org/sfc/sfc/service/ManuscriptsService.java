package org.sfc.sfc.service;

import org.sfc.sfc.comon.Result;
import org.sfc.sfc.entity.Manuscripts;
import com.baomidou.mybatisplus.extension.service.IService;
import org.sfc.sfc.vo.ManuscriptsVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
public interface ManuscriptsService extends IService<Manuscripts> {
    /**
     * 创建稿件
     * @param manuscriptsVo
     * @return
     */
    Result<?> createManuscripts(ManuscriptsVo manuscriptsVo);

    /**
     * 分页查询
     * @return
     */
    Result<?> pageList(Integer pageNum,Integer pageSize,ManuscriptsVo manuscriptsVo);

    /**
     * 获取详情
     * @param id
     * @return
     */
    Result<?> getDetailById(Long id);

    /**
     * 编辑稿件
     * @param manuscriptsVo
     * @return
     */
    Result<?> editManuscripts(ManuscriptsVo manuscriptsVo);

    /**
     * 提交一审
     * @param id
     * @return
     */
    Result<?> submitPending1(Long id);

    /**
     * 审核通过
     * @param id
     * @return
     */
    Result<?> approve(Long id);

    /**
     * 退回
     * @return
     */
    Result<?> reject(ManuscriptsVo manuscriptsVo);

    /**
     * 重提
     * @param id
     * @return
     */
    Result<?> resubmit(Long id);
}
