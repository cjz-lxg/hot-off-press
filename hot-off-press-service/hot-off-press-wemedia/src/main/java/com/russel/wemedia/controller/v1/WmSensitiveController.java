package com.russel.wemedia.controller.v1;

import com.russel.model.common.dtos.ResponseResult;
import com.russel.model.wemedia.dtos.WmSensitiveDto;
import com.russel.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Russel
 * @DATE 2023/11/8.
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {

    @Autowired
    private WmSensitiveService wmSensitiveService;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody WmSensitiveDto dto) {
        return wmSensitiveService.list(dto);
    }
}
