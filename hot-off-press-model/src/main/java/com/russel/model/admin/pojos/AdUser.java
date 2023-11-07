package com.russel.model.admin.pojos;

/**
 * @author Russel
 * @DATE 2023/11/7.
 */
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("ad_user")
public class AdUser implements Serializable {

    private static final long serialVersionUID = 8967081618773229141L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
    private String password;
    private String salt;
    private String nickname;
    private String image;
    private String phone;
    private Integer status;
    private String email;
    private Date loginTime;
    private Date createdTime;
}

