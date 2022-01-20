package com.baiyi.opscloud.packer.workorder;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrder;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderVO;
import com.baiyi.opscloud.service.workorder.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/6 1:47 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderPacker {

    private final WorkOrderService workOrderService;

    public WorkOrderVO.Group wrap(WorkOrderGroup workOrderGroup) {
        WorkOrderVO.Group group = BeanCopierUtil.copyProperties(workOrderGroup, WorkOrderVO.Group.class);
        List<WorkOrder> workOrders = workOrderService.queryByWorkOrderGroupId(group.getId());
        group.setWorkOrders(
                workOrders.stream().map(workOrder -> {
                    WorkOrderVO.WorkOrder workOrderVO = BeanCopierUtil.copyProperties(workOrder, WorkOrderVO.WorkOrder.class);
                    workOrderVO.setLoading(false);
                    return workOrderVO;
                }).collect(Collectors.toList())
        );
        return group;
    }

    /**
     * 包装工单
     *
     * @param iWorkOrder
     */
    public void wrap(WorkOrderVO.IWorkOrder iWorkOrder) {
        WorkOrder workOrder = workOrderService.getById(iWorkOrder.getWorkOrderId());
        WorkOrderVO.WorkOrder workOrderVO = BeanCopierUtil.copyProperties(workOrder, WorkOrderVO.WorkOrder.class);
        iWorkOrder.setWorkOrder(workOrderVO);
    }


}
