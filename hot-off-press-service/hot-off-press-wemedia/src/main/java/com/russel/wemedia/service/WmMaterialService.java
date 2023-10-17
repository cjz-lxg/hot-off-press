package com.russel.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.dtos.WmMaterialDto;
import com.russel.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

/**
 * author by Russel
 * created by 2023/10/17.
 */
public interface WmMaterialService extends IService<WmMaterial> {

    public ResponseResult saveMaterial(MultipartFile multipartFile);

    ResponseResult findList(WmMaterialDto wmMaterialDto);
}
