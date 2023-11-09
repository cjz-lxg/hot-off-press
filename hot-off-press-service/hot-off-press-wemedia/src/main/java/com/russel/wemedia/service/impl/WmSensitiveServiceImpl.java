package com.russel.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.model.common.dtos.PageResponseResult;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.common.enums.AppHttpCodeEnum;
import com.russel.model.wemedia.dtos.WmSensitiveDto;
import com.russel.model.wemedia.pojos.WmSensitive;
import com.russel.wemedia.mapper.WmSensitiveMapper;
import com.russel.wemedia.service.WmSensitiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Russel
 * @DATE 2023/11/8.
 */
@Service
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive> implements WmSensitiveService {

    @Override
    public ResponseResult list(WmSensitiveDto dto) {
        //1. 检查参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();

        //2.分页查询
        Page<WmSensitive> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmSensitive> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            queryWrapper.like(WmSensitive::getSensitives, dto.getName());
        }
        page = page(page, queryWrapper);
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }
}
