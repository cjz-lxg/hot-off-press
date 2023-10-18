package com.russel.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.common.constants.WemediaConstants;
import com.russel.common.exception.CustomException;
import com.russel.model.common.dtos.PageResponseResult;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.common.enums.AppHttpCodeEnum;
import com.russel.model.wemedia.dtos.WmNews;
import com.russel.model.wemedia.dtos.WmNewsDto;
import com.russel.model.wemedia.pojos.WmMaterial;
import com.russel.model.wemedia.pojos.WmNewsMaterial;
import com.russel.model.wemedia.pojos.WmNewsPageReqDto;
import com.russel.model.wemedia.pojos.WmUser;
import com.russel.utils.thread.WmThreadLocalUtil;
import com.russel.wemedia.mapper.WmMaterialMapper;
import com.russel.wemedia.mapper.WmNewsMapper;
import com.russel.wemedia.mapper.WmNewsMaterialMapper;
import com.russel.wemedia.service.WmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Russel
 * @DATE 2023/10/18.
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Override
    public ResponseResult findAll(WmNewsPageReqDto dto) {
        //1. check the argument
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        WmUser user = WmThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2. query the page data
        Page<WmNews> page = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //according to the status
        if (dto.getStatus() != null) {
            lambdaQueryWrapper.eq(WmNews::getStatus, dto.getStatus());
        }
        //according to the channel
        if (dto.getChannelId() != null) {
            lambdaQueryWrapper.eq(WmNews::getChannelId, dto.getChannelId());
        }
        //according to the time period
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
            lambdaQueryWrapper.between(WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());
        }
        //according to the keyword
        if (StringUtils.isNoneBlank(dto.getKeyword())) {
            lambdaQueryWrapper.like(WmNews::getTitle, dto.getKeyword());
        }
        //according to the user
        lambdaQueryWrapper.eq(WmNews::getUserId, user.getId());

        //according to the time to sort the result des
        lambdaQueryWrapper.orderByDesc(WmNews::getCreatedTime);

        page = page(page, lambdaQueryWrapper);


        //3. return result
        PageResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    @Override
    @Transactional
    public ResponseResult submitNews(WmNewsDto dto) {
        //1. check the condition
        if (dto == null || dto.getContent() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2. save or modify the news
        WmNews wmNews = new WmNews();
        //copy the data from dto to the instance
        copyPropertyFromDto(dto, wmNews);
        // if the cur cover was auto , then set the Type to null
        if (wmNews.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)) {
            wmNews.setType(null);
        }
        saveOrUpdateWmNews(wmNews);

        //3. check if it is draft , then end the fun
        if (wmNews.getStatus().equals(WmNews.Status.NORMAL.getCode())) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //4. if not , save relation of the cover photo and  material
        // gain the information of content of press
        List<String> material = extractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(material, wmNews.getId());

        //5.save relation between the cover photo and material , if auto , should match the cover photo
        saveRelativeInfoForCover(dto, wmNews, material);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {

        List<String> images = dto.getImages();

        //if the type of cover was auto , set the type of data
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            if(materials.size() >= 3){
                //multi
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            }else if(!materials.isEmpty()){
                //single
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            }else {
                //none
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }

            //modify press
            if(images != null && !images.isEmpty()){
                wmNews.setImages(StringUtils.join(images,","));
            }
            updateById(wmNews);
        }
        if(images != null && !images.isEmpty()){
            saveRelativeInfo(images,wmNews.getId(),WemediaConstants.WM_COVER_REFERENCE);
        }

    }

    private void saveOrUpdateWmNews(WmNews wmNews) {
        //fulfill the property
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short) 1);

        if (wmNews.getId() == null) {
            //save
            save(wmNews);
        } else {
            //modify
            //delete the relation of cover photo and material
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId, wmNews.getId()));
            updateById(wmNews);
        }
    }

    private void saveRelativeInfoForContent(List<String> material, Integer id) {

    }

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    @Autowired
    WmNewsMaterialMapper wmNewsMaterialMapper;

    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if (materials == null || materials.isEmpty()) {
            return;
        }
        //searching for the url of photo depending on the id of material
        List<WmMaterial> dbMaterial = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, materials));
        //check if the material  valid
        if (dbMaterial == null || dbMaterial.isEmpty() || materials.size() != dbMaterial.size()) {
            //throw the error
            // first it can suggest the caller that material is unvalid ,
            //second , it can roll back the data
            throw new CustomException(AppHttpCodeEnum.MATERIAL_REFERENCE_FAIL);
        }
        List<Integer> idList = dbMaterial.stream().map(WmMaterial::getId).collect(Collectors.toList());

        //batch save
        wmNewsMaterialMapper.saveRelations(idList, newsId, type);
    }


    private List<String> extractUrlInfo(String content) {
        ArrayList<String> material = new ArrayList<>();
        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if (map.get("type").equals("image")) {
                String imgUrl = (String) map.get("value");
                material.add(imgUrl);
            }
        }
        return material;
    }

    private void copyPropertyFromDto(WmNewsDto dto, WmNews wmNews) {
        wmNews.setId(dto.getId());
        wmNews.setTitle(dto.getTitle());
        wmNews.setChannelId(dto.getChannelId());
        wmNews.setLabels(dto.getLabels());
        wmNews.setPublishTime(dto.getPublishTime());
        wmNews.setContent(dto.getContent());
        wmNews.setType(dto.getType());
        wmNews.setSubmitedTime(wmNews.getSubmitedTime());
        wmNews.setStatus(dto.getStatus());
        //make the list to the str
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            String imageStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imageStr);
        }
    }
}
