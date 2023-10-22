package com.russel.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.russel.model.article.dtos.ArticleDto;
import com.russel.model.article.dtos.ArticleHomeDto;
import com.russel.model.article.pojos.ApArticle;
import com.russel.model.common.dtos.ResponseResult;
import org.apache.commons.net.nntp.Article;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 根据参数加载文章列表
     * @param loadType 1为加载更多  2为加载最新
     * @param dto
     * @return
     */
    ResponseResult load(Short loadType, ArticleHomeDto dto);

    ResponseResult saveArticle(ArticleDto dto);
}
