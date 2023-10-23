package com.russel.article.service;

import com.russel.model.article.pojos.ApArticle;

/**
 * @author Russel
 * @DATE 2023/10/23.
 */
public interface ArticleFreemarkerService {
    public void buildArticleToMinIO(ApArticle apArticle, String content);
}
