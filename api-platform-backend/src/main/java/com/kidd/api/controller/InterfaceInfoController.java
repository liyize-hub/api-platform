package com.kidd.api.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kidd.api.annotation.AuthCheck;
import com.kidd.api.common.BaseResponse;
import com.kidd.api.common.DeleteRequest;
import com.kidd.api.common.ErrorCode;
import com.kidd.api.common.ResultUtils;
import com.kidd.api.constant.CommonConstant;
import com.kidd.api.constant.UserConstant;
import com.kidd.api.exception.BusinessException;
import com.kidd.api.exception.ThrowUtils;
import com.kidd.api.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.kidd.api.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.kidd.api.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.kidd.api.model.entity.InterfaceInfo;
import com.kidd.api.model.entity.User;
import com.kidd.api.service.InterfaceInfoService;
import com.kidd.api.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest,
            HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);

        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);

        // 获取登录用户
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());

        boolean result = interfaceInfoService.save(interfaceInfo);

        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest,
            HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();

        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(
            @RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);

        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();

        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(
            @RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {

        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }

        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);

        return ResultUtils.success(interfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(
            @RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {

        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();

        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);

        // 限制爬虫
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR);

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description)
                .orderBy(StringUtils.isNotBlank(sortField),
                        sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    // /**
    // * 分页获取列表（封装类）
    // *
    // * @param interfaceInfoQueryRequest
    // * @param request
    // * @return
    // */
    // @PostMapping("/list/page/vo")
    // public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(
    // @RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
    // HttpServletRequest request) {
    // long current = interfaceInfoQueryRequest.getCurrent();
    // long size = interfaceInfoQueryRequest.getPageSize();
    // // 限制爬虫
    // ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
    // Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new
    // Page<>(current, size),
    // interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
    // return
    // ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage,
    // request));
    // }

    // /**
    // * 分页获取当前用户创建的资源列表
    // *
    // * @param interfaceInfoQueryRequest
    // * @param request
    // * @return
    // */
    // @PostMapping("/my/list/page/vo")
    // public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(
    // @RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
    // HttpServletRequest request) {
    // if (interfaceInfoQueryRequest == null) {
    // throw new BusinessException(ErrorCode.PARAMS_ERROR);
    // }
    // User loginUser = userService.getLoginUser(request);
    // interfaceInfoQueryRequest.setUserId(loginUser.getId());
    // long current = interfaceInfoQueryRequest.getCurrent();
    // long size = interfaceInfoQueryRequest.getPageSize();
    // // 限制爬虫
    // ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
    // Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new
    // Page<>(current, size),
    // interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
    // return
    // ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage,
    // request));
    // }

    // // endregion

    // /**
    // * 分页搜索（从 ES 查询，封装类）
    // *
    // * @param interfaceInfoQueryRequest
    // * @param request
    // * @return
    // */
    // @PostMapping("/search/page/vo")
    // public BaseResponse<Page<InterfaceInfoVO>> searchInterfaceInfoVOByPage(
    // @RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
    // HttpServletRequest request) {
    // long size = interfaceInfoQueryRequest.getPageSize();
    // // 限制爬虫
    // ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
    // Page<InterfaceInfo> interfaceInfoPage =
    // interfaceInfoService.searchFromEs(interfaceInfoQueryRequest);
    // return
    // ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage,
    // request));
    // }

    // /**
    // * 编辑（用户）
    // *
    // * @param interfaceInfoEditRequest
    // * @param request
    // * @return
    // */
    // @PostMapping("/edit")
    // public BaseResponse<Boolean> editInterfaceInfo(@RequestBody
    // InterfaceInfoEditRequest interfaceInfoEditRequest,
    // HttpServletRequest request) {
    // if (interfaceInfoEditRequest == null || interfaceInfoEditRequest.getId() <=
    // 0) {
    // throw new BusinessException(ErrorCode.PARAMS_ERROR);
    // }
    // InterfaceInfo interfaceInfo = new InterfaceInfo();
    // BeanUtils.copyProperties(interfaceInfoEditRequest, interfaceInfo);
    // List<String> tags = interfaceInfoEditRequest.getTags();
    // if (tags != null) {
    // interfaceInfo.setTags(JSONUtil.toJsonStr(tags));
    // }
    // // 参数校验
    // interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
    // User loginUser = userService.getLoginUser(request);
    // long id = interfaceInfoEditRequest.getId();
    // // 判断是否存在
    // InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
    // ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
    // // 仅本人或管理员可编辑
    // if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) &&
    // !userService.isAdmin(loginUser)) {
    // throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    // }
    // boolean result = interfaceInfoService.updateById(interfaceInfo);
    // return ResultUtils.success(result);
    // }

}
