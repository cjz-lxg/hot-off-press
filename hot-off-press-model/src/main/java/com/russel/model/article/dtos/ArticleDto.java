package com.russel.model.article.dtos;

import com.russel.model.article.pojos.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Russel
 * @DATE 2023/10/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDto extends ApArticle {

    private static final long serialVersionUID = -2590161942205139428L;

    /**
     * 文章内容
     */
    private String content;
}
