package com.russel.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.model.admin.dtos.AdUserDto;
import com.russel.model.admin.pojos.AdUser;
import com.russel.model.common.dtos.ResponseResult;


/**
 * @author Russel
 * @DATE 2023/11/7.
 */
public interface AdUserService {

    public ResponseResult login(AdUserDto dto);

}
