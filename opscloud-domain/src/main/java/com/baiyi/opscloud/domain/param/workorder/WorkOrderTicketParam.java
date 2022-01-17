package com.baiyi.opscloud.domain.param.workorder;

import com.baiyi.opscloud.domain.vo.workorder.WorkflowVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2022/1/10 2:42 PM
 * @Version 1.0
 */
public class WorkOrderTicketParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class CreateTicket {
        @NotBlank(message = "工单Key不能为空！")
        private String workOrderKey;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @ApiModel
    public static class SubmitTicket extends SaveTicket {
        private static final long serialVersionUID = -925558688216913781L;
    }

    @Data
    @ApiModel
    public static class SaveTicket implements Serializable {

        private static final long serialVersionUID = -608339787175813785L;
        @NotNull(message = "必须指定工单票据ID")
        @ApiModelProperty(value = "工单票据ID")
        private Integer ticketId;

        @ApiModelProperty(value = "工单说明")
        private String comment;

        @ApiModelProperty(value = "工作流")
        private WorkflowVO.WorkflowView workflowView;

    }

}
