package com.russel.model.wemedia.dtos;

import com.russel.model.common.dtos.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * author by Russel
 * created by 2023/10/17.
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class WmMaterialDto extends PageRequestDto {

    /**
     * 1 收藏
     * 0 未收藏
     */
    private Short isCollection;
}
