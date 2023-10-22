package com.russel.article.feign;

import com.russel.apis.article.IArticleClient;
import com.russel.article.service.ApArticleService;
import com.russel.model.article.dtos.ArticleDto;
import com.russel.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Russel
 * @DATE 2023/10/22.
 */
@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApArticleService service;

    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return service.saveArticle(dto);
    }
}
