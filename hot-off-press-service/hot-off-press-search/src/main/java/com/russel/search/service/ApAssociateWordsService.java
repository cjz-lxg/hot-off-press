package com.russel.search.service;

import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.search.dtos.UserSearchDto;

/**
 * @author Russel
 * @DATE 2023/11/6.
 */
public interface ApAssociateWordsService {

    /**
     联想词
     @param userSearchDto
     @return
     */
    ResponseResult findAssociate(UserSearchDto userSearchDto);

}
