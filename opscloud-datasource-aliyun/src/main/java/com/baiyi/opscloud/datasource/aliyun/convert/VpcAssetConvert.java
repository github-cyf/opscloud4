package com.baiyi.opscloud.datasource.aliyun.convert;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.builder.asset.AssetContainerBuilder;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;

import static com.baiyi.opscloud.datasource.aliyun.convert.ComputeAssetConvert.toGmtDate;

/**
 * @Author 修远
 * @Date 2021/6/23 1:28 下午
 * @Since 1.0
 */
public class VpcAssetConvert {

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVpcsResponse.Vpc entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getVpcId()) // 资产id = 实例id
                .name(entity.getVpcName())
                .assetKey(entity.getVpcId())
                // cidrBlock
                .assetKey2(entity.getCidrBlock())
                .kind("aliyunVpc")
                .assetType(DsAssetTypeEnum.VPC.name())
                .regionId(entity.getRegionId())
                .description(entity.getDescription())
                .createdTime(toGmtDate(entity.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entity.getIsDefault())
                .paramProperty("vRouterId", entity.getVRouterId())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeSecurityGroupsResponse.SecurityGroup entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getSecurityGroupId()) // 资产id = 实例id
                .name(entity.getSecurityGroupName())
                .assetKey(entity.getSecurityGroupId())
                .kind("aliyunSecurityGroup")
                .assetType(DsAssetTypeEnum.ECS_SG.name())
                .description(entity.getDescription())
                .createdTime(toGmtDate(entity.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("securityGroupType", entity.getSecurityGroupType())
                .build();
    }

    public static AssetContainer toAssetContainer(DatasourceInstance dsInstance, DescribeVSwitchesResponse.VSwitch entity) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(dsInstance.getUuid())
                .assetId(entity.getVSwitchId()) // 资产id = 实例id
                .name(entity.getVSwitchName())
                .assetKey(entity.getVSwitchId())
                // cidrBlock
                .assetKey2(entity.getCidrBlock())
                .kind("aliyunVSwitch")
                .assetType(DsAssetTypeEnum.V_SWITCH.name())
                .zone(entity.getZoneId())
                .description(entity.getDescription())
                .createdTime(toGmtDate(entity.getCreationTime()))
                .build();

        return AssetContainerBuilder.newBuilder()
                .paramAsset(asset)
                .paramProperty("isDefault", entity.getIsDefault())
                .build();
    }
}
