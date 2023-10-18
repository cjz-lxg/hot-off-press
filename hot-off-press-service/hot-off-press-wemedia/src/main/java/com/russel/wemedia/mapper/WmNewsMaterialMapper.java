package com.russel.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.russel.model.wemedia.pojos.WmNewsMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author by Russel
 * created by 2023/10/18.
 */
public interface WmNewsMaterialMapper extends BaseMapper<WmNewsMaterial> {

    void saveRelations(@Param("materialIds") List<Integer> materialIds, @Param("newsId") Integer newsId,
                       @Param("type") Short type);

}
