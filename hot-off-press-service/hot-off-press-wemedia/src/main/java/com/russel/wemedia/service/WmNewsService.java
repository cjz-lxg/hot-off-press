package com.russel.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.dtos.WmNews;
import com.russel.model.wemedia.dtos.WmNewsDto;
import com.russel.model.wemedia.pojos.WmNewsPageReqDto;

/**
 * author by Russel
 * created by 2023/10/18.
 */
public interface WmNewsService extends IService<WmNews> {

    public ResponseResult findAll(WmNewsPageReqDto dto);

    public ResponseResult submitNews(WmNewsDto dto);

}
