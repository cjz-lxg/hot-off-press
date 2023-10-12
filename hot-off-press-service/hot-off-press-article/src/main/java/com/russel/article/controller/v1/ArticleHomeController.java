package com.russel.article.controller.v1;


import com.russel.article.service.ApArticleService;
import com.russel.common.constants.ArticleConstants;
import com.russel.model.article.dtos.ArticleHomeDto;
import com.russel.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/article")
public class ArticleHomeController {

    @Autowired
    private ApArticleService apArticleService;

    @RequestMapping("load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
    }

    @RequestMapping("loadmore")
    public ResponseResult loadMore(@RequestBody ArticleHomeDto dto) {
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
    }

    @RequestMapping("loadnew")
    public ResponseResult loadNew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(ArticleConstants.LOADTYPE_LOAD_NEW, dto);
    }
}
