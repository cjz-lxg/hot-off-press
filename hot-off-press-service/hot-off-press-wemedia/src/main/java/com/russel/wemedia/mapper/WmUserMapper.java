package com.russel.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.russel.model.wemedia.pojos.WmUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WmUserMapper extends BaseMapper<WmUser> {
}