package com.russel.model.article.dtos;

import lombok.Data;

import java.util.Date;


@Data
public class ArticleHomeDto {
    // 最大时间
    Date maxBehotTime;
    // 最小时间
    Date minBehotTime;
    // 分页size
    Short size;
    // 频道ID
    String tag;
}
