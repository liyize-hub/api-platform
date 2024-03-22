package com.kidd.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kidd.api.common.ErrorCode;
import com.kidd.api.exception.BusinessException;
import com.kidd.api.exception.ThrowUtils;
import com.kidd.api.mapper.InterfaceInfoMapper;
import com.kidd.api.model.entity.InterfaceInfo;
import com.kidd.api.service.InterfaceInfoService;

/**
 * 接口信息服务实现类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
                implements InterfaceInfoService {

        @Override
        public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
                if (interfaceInfo == null) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR);
                }

                String name = interfaceInfo.getName();

                // 创建时，参数不能为空
                if (add) {
                        ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
                }
                // 有参数则校验
                if (StringUtils.isNotBlank(name) && name.length() > 50) {
                        throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
                }
        }

}
