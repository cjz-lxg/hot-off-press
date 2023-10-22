package com.russel.wemedia.service.impl;

import com.russel.apis.article.IArticleClient;
import com.russel.model.article.dtos.ArticleDto;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.dtos.WmNews;
import com.russel.model.wemedia.pojos.WmChannel;
import com.russel.model.wemedia.pojos.WmUser;
import com.russel.wemedia.mapper.WmChannelMapper;
import com.russel.wemedia.mapper.WmNewsMapper;
import com.russel.wemedia.mapper.WmUserMapper;
import com.russel.wemedia.service.WmNewsAutoScanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Russel
 * @DATE 2023/10/22.
 */
@Service
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Override
    //@Async
    public void autoScanWmNews(Integer id) {
        //TODO 后期引入阿里云的内容审核
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews==null){
            throw new RuntimeException("WmNewsAutoScanServiceImpl.autoScanWmNews - wmNews is null");
        }
        if (wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            ResponseResult responseResult = saveAppArticle(wmNews);
            if(!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl-文章审核，保存app端相关文章数据失败");
            }
            //回填article_id
            wmNews.setArticleId((Long) responseResult.getData());
            updateWmNews(wmNews,(short) 9,"审核成功");
        }
    }

    @Autowired
    private WmChannelMapper wmChannelMapper;
    @Autowired
    private WmUserMapper wmUserMapper;
    @Autowired
    private IArticleClient articleClient;

    private ResponseResult saveAppArticle(WmNews wmNews) {

        ArticleDto dto = new ArticleDto();
        //属性的拷贝
        BeanUtils.copyProperties(wmNews,dto);
        //文章的布局
        dto.setLayout(wmNews.getType());
        //频道
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel != null){
            dto.setChannelName(wmChannel.getName());
        }

        //作者
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser != null){
            dto.setAuthorName(wmUser.getName());
        }

        //设置文章id
        if(wmNews.getArticleId() != null){
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;

    }

    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }
}
