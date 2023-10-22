package com.russel.apis.article.fallback;

import com.russel.apis.article.IArticleClient;
import com.russel.model.article.dtos.ArticleDto;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

/**
 * @author Russel
 * @DATE 2023/10/22.
 */
@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto)  {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取数据失败");
    }
}
