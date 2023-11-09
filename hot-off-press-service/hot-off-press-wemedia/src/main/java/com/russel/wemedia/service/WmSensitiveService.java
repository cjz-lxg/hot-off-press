package com.russel.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.dtos.WmSensitiveDto;
import com.russel.model.wemedia.pojos.WmSensitive;

/**
 * author by Russel
 * created by 2023/11/8.
 */
public interface WmSensitiveService {
    public ResponseResult list(WmSensitiveDto dto);
}
