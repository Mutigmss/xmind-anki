package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDynamicsDto1 {


    //动态标题
    private String title;
    //动态内容

    private String content;

    private Integer number;

}
