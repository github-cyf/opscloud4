package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:50 下午
 * @Version 1.0
 */
public class UserGroupParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "用户组名称")
        private String name;

        @ApiModelProperty(value = "用户组类型",example = "1")
        private Integer grpType;

        @ApiModelProperty(value = "允许工作流申请",example = "1")
        private Integer workflow;

    }
}
