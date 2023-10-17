package com.russel.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.pojos.WmChannel;

/**
 * @author Russel
 * @DATE 2023/10/17.
 */
public interface WmChannelService extends IService<WmChannel> {

    ResponseResult findAll();

}
