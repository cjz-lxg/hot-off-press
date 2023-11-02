package com.russel.search.service;

/**
 * author by Russel
 * created by 2023/11/2.
 */
public interface ApUserSearchService {

    /**
     * 保存用户搜索历史记录
     * @param keyword
     * @param userId
     */
    public void insert(String keyword,Integer userId);

}
