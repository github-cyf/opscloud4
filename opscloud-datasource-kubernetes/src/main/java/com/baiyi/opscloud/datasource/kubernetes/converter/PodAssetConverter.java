package com.baiyi.opscloud.datasource.kubernetes.converter;

import com.baiyi.opscloud.common.util.time.AgoUtil;
import com.baiyi.opscloud.core.util.TimeUtil;
import com.baiyi.opscloud.core.util.enums.TimeZoneEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import io.fabric8.kubernetes.api.model.*;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/25 1:16 下午
 * @Version 1.0
 */
public class PodAssetConverter {

    public static Date toGmtDate(String time) {
        return TimeUtil.toGmtDate(time, TimeZoneEnum.UTC);
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, Pod entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getMetadata().getUid()) // 资产id
                .name(entity.getMetadata().getName())
                .assetKey(entity.getStatus().getPodIP())    // podIp
                .assetKey2(entity.getMetadata().getNamespace()) // namespace
                .kind(entity.getKind())
                .assetType(DsAssetTypeConstants.KUBERNETES_POD.name())
                .createdTime(toGmtDate(entity.getMetadata().getCreationTimestamp()))
                .build();

        Date startTime = toGmtDate(entity.getStatus().getStartTime());
        Map<String, Boolean> podStatusMap = entity.getStatus().getConditions()
                .stream().collect(Collectors.toMap(PodCondition::getType, a -> Boolean.valueOf(a.getStatus()), (k1, k2) -> k1));
        Optional<String> statusOptional = podStatusMap.keySet().stream().filter(k -> !podStatusMap.get(k)).findFirst();
        ContainerStatus containerStatus = CollectionUtils.isEmpty(entity.getStatus().getContainerStatuses()) ?
                new ContainerStatus("-", "-", "-", new ContainerState(), "-", false, 0, false, new ContainerState())
                : entity.getStatus().getContainerStatuses().get(0);
        AssetContainer assetContainer = AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramChildren(toChildren(entity))
                .paramProperty("phase", entity.getStatus().getPhase())
                .paramProperty("startTime", com.baiyi.opscloud.common.util.TimeUtil.dateToStr(startTime))
                .paramProperty("nodeName", entity.getSpec().getNodeName())
                .paramProperty("restartCount", containerStatus.getRestartCount())
                .paramProperty("hostIp", entity.getStatus().getHostIP())
                .paramProperty("image", containerStatus.getImage())
                .paramProperty("imageId", containerStatus.getImageID())
                .paramProperty("containerId", containerStatus.getContainerID())
                .paramProperty("podScheduled", podStatusMap.get("PodScheduled"))
                .paramProperty("containersReady", podStatusMap.get("ContainersReady"))
                .paramProperty("initialized", podStatusMap.get("Initialized"))
                .paramProperty("ready", podStatusMap.get("Ready"))
                .paramProperty("status", !statusOptional.isPresent())
                .build();
        assetContainer.setAgo(AgoUtil.format(startTime));
        return assetContainer;
    }

    private static List<AssetContainer> toChildren(Pod pod) {
        List<Container> containers = pod.getSpec().getContainers();
        if (CollectionUtils.isEmpty(containers)) return null;
        return containers.stream().map(c -> {
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .name(c.getName())
                    .assetKey(pod.getMetadata().getName()) // podName
                    .build();
            return AssetContainerBuilder.newBuilder()
                    .paramAsset(asset).build();
        }).collect(Collectors.toList());
    }

}
