package com.russel.search.service;

import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.search.dtos.UserSearchDto;

import java.io.IOException;

/**
 * author by Russel
 * created by 2023/11/1.
 */
public interface ArticleSearchService {

    /**
     ES文章分页搜索
     @return
     */
    ResponseResult search(UserSearchDto userSearchDto) throws IOException;

}
