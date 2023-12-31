package com.russel.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.file.service.FileStorageService;
import com.russel.model.common.dtos.PageResponseResult;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.common.enums.AppHttpCodeEnum;
import com.russel.model.wemedia.dtos.WmMaterialDto;
import com.russel.model.wemedia.pojos.WmMaterial;
import com.russel.utils.thread.WmThreadLocalUtil;
import com.russel.wemedia.mapper.WmMaterialMapper;
import com.russel.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * author by Russel
 * created by 2023/10/17.
 */
@Slf4j
@Service
@Transactional
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ResponseResult findList(WmMaterialDto wmMaterialDto) {
        //1.check the argument
        wmMaterialDto.checkParam();

        //2.query the page data
        Page<WmMaterial> page = new Page<>(wmMaterialDto.getPage(), wmMaterialDto.getSize());
        LambdaQueryWrapper<WmMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // is collected ?
        if (wmMaterialDto.getIsCollection() != null && wmMaterialDto.getIsCollection() == 1) {
            lambdaQueryWrapper.eq(WmMaterial::getIsCollection, wmMaterialDto.getIsCollection());
        }

        // according to the user
        lambdaQueryWrapper.eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId());

        //according to the time, to sort the result
        lambdaQueryWrapper.orderByDesc(WmMaterial::getCreatedTime);

        page = page(page, lambdaQueryWrapper);

        //3.return the result
        PageResponseResult responseResult = new PageResponseResult(wmMaterialDto.getPage(), wmMaterialDto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;

    }

    @Override
    public ResponseResult saveMaterial(MultipartFile multipartFile) {
        //TODO 这里需要修改一下,当任意一个发生错误或者异常时,需要回滚
        //1.check the argument
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.save the file in the minio
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileUrl = null;
        try {
            fileUrl = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
        } catch (IOException e) {
            log.error("an error occurs in the uploading file to the minio:{}", e.getMessage());
        }

        //3.save the file info in the database
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        wmMaterial.setUrl(fileUrl);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setType((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

        //4. return the result
        return ResponseResult.okResult(wmMaterial);
    }
}
