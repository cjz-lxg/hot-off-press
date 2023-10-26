package com.russel.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.russel.model.article.pojos.ApArticleConfig;

import java.util.Map;

/**
 * @author Russel
 * @DATE 2023/10/26.
 */
public interface ApArticleConfigService extends IService<ApArticleConfig> {

    /**
     * 修改文章配置
     * @param map
     */
    public void updateByMap(Map map);
}
