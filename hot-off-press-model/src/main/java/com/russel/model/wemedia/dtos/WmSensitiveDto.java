package com.russel.model.wemedia.dtos;

import com.russel.model.common.dtos.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Russel
 * @DATE 2023/11/8.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmSensitiveDto extends PageRequestDto {
    String name;
}
