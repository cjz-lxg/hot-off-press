package com.russel.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.russel.es.pojo.SearchArticleVo;
import com.russel.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    public List<SearchArticleVo> loadArticleList();

}
