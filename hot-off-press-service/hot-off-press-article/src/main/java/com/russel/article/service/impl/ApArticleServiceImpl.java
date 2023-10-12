package com.russel.article.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.article.mapper.ApArticleMapper;
import com.russel.article.service.ApArticleService;
import com.russel.common.constants.ArticleConstants;
import com.russel.model.article.dtos.ArticleHomeDto;
import com.russel.model.article.pojos.ApArticle;
import com.russel.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    private final static short MAX_PAGE_SIZE = 50;

    private final static short DEFAULT_PAGE_SIZE = 50;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public ResponseResult load(Short loadType, ArticleHomeDto dto) {
        //1.校验参数
        Short size = dto.getSize();
        if (size==null || size<=0){
            size = DEFAULT_PAGE_SIZE;
        }

        size = (short) Math.min(size, MAX_PAGE_SIZE);
        dto.setSize(size);

        //2.类型参数校验
        if (!loadType.equals(ArticleConstants.LOADTYPE_LOAD_MORE) || !loadType.equals(ArticleConstants.LOADTYPE_LOAD_NEW)) {
            loadType = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        //3.文章频道参数校验
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        //4.时间参数校验
        if (dto.getMaxBehotTime()==null){
            dto.setMaxBehotTime(new Date());
        }
        if (dto.getMinBehotTime()==null){
            dto.setMinBehotTime(new Date());
        }

        //5.查询数据
        List<ApArticle> apArticles = apArticleMapper.loadArticleList(dto, loadType);

        //6.返回结果
        return ResponseResult.okResult(apArticles);
    }
}
