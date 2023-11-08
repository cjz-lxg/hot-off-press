package com.russel.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.russel.admin.mapper.AdUserMapper;
import com.russel.admin.service.AdUserService;
import com.russel.model.admin.dtos.AdUserDto;
import com.russel.model.admin.pojos.AdUser;
import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.common.enums.AppHttpCodeEnum;
import com.russel.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Russel
 * @DATE 2023/11/7.
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {
    @Override
    public ResponseResult login(AdUserDto dto) {
        //1. 判定用户名为空
        if (dto == null || StringUtils.isBlank(dto.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 2.根据用户名获取对象
        AdUser user = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, dto.getName()));

        //3.查看用户是否存在
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        //4.查看密码是否相同
        String salt = user.getSalt();
        String password = dto.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        if (!user.getPassword().equals(md5Password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        //5.组装返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        String token = AppJwtUtil.getToken(user.getId().longValue());
        map.put("token", token);

        return ResponseResult.okResult(map);
    }
}
