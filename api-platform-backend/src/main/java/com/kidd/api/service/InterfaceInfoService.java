package com.kidd.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kidd.api.model.entity.InterfaceInfo;

public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
