package com.russel.wemedia.service;

import io.swagger.models.auth.In;

/**
 * @author Russel
 * @DATE 2023/10/22.
 */
public interface WmNewsAutoScanService {

        /**
        * 自动审核
        */
        public void autoScanWmNews(Integer id);
}
