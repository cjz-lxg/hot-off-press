package com.russel.apis.article;

import com.russel.model.article.dtos.ArticleDto;
import com.russel.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author by Russel
 * created by 2023/10/21.
 */

@FeignClient(value = "hot-off-press-article")
public interface IArticleClient {
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto);
}
