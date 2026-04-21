package org.sfc.sfc.controller;

import jakarta.annotation.Resource;
import org.sfc.sfc.annotation.RequireRole;
import org.sfc.sfc.comon.Result;
import org.sfc.sfc.comon.SysRoleEnum;
import org.sfc.sfc.service.ManuscriptsService;
import org.sfc.sfc.vo.ManuscriptsVo;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 稿件主表 前端控制器
 * </p>
 *
 * @author nfc
 * @since 2026-04-21
 */
@RestController
@RequestMapping("/manuscripts")
public class ManuscriptsController {
    @Resource
    private ManuscriptsService manuscriptsService;

    /**
     * 创建稿件
     * @param manuscriptsVo 增删查改参数实体
     *RequireRole控制接口权限，具体角色在SysRoleEnum枚举类
     *                      这里设置只有【作者】这个角色可以创建
     * @return
     */
    @PostMapping("/createManuscripts")
    @RequireRole(SysRoleEnum.EDITOR)
    public Result<?> createManuscripts(@RequestBody ManuscriptsVo manuscriptsVo){

        return manuscriptsService.createManuscripts(manuscriptsVo);
    }

    /**
     * 分页查询稿件信息
     * @param pageNum 当前页数
     * @param pageSize 每页数据条数
     * @param manuscriptsVo 查询参数实体
     * @return
     */
    @GetMapping("/pageList")
    Result<?> pageList(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10")Integer pageSize,
                       @RequestBody ManuscriptsVo manuscriptsVo){
        return manuscriptsService.pageList(pageNum,pageSize,manuscriptsVo);
    }

    /**
     * 获取稿件详情，包括流转历史记录
     * @param id
     * @return
     */
    @GetMapping("/getDetailById")
    Result<?> getDetailById(@RequestParam(required = true) Long id){
        return manuscriptsService.getDetailById(id);
    }

    /**
     * 编辑稿件，只改变基础信息，不会改变流程节点相关信息
     * @param manuscriptsVo
     * @return
     */
    @PutMapping("/editManuscripts")
    Result<?> editManuscripts(@RequestBody ManuscriptsVo manuscriptsVo){
        return manuscriptsService.editManuscripts(manuscriptsVo);
    }

    /**
     * 提交一审
     * @param id
     * @return
     */
    @PostMapping("/submitPending1")
    Result<?> submitPending1(@RequestParam(required = true) Long id){
        return manuscriptsService.submitPending1(id);
    }

    /**
     * 审核通过
     * @param id
     * @return
     */
    @PostMapping("/approve")
    Result<?> approve(@RequestParam(required = true) Long id){
        return manuscriptsService.approve(id);
    }

    /**
     * 重提
     * @param id
     * @return
     */
    @PostMapping("/resubmit")
    Result<?> resubmit(@RequestParam(required = true) Long id){
        return manuscriptsService.resubmit(id);
    }

    /**
     * 退回
     * @param manuscriptsVo
     * @return
     */
    @PostMapping("/reject")
    Result<?> reject(@RequestBody ManuscriptsVo manuscriptsVo){
        return manuscriptsService.reject(manuscriptsVo);
    }
}
