package com.russel.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.pojos.WmChannel;
import com.russel.wemedia.mapper.WmChannelMapper;
import com.russel.wemedia.service.WmChannelService;
import org.springframework.stereotype.Service;

/**
 * @author Russel
 * @DATE 2023/10/17.
 */
@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {

    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(this.list());
    }
}
