package com.russel.admin.controller.v1;

import com.russel.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Russel
 * @DATE 2023/11/8.
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController {

    @DeleteMapping("/del/{id}")
    public ResponseResult deleteById(@PathVariable Integer id) {
        return null;
    }

}
