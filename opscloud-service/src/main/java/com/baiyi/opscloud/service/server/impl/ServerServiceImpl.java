package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.factory.business.base.AbstractBusinessService;
import com.baiyi.opscloud.mapper.opscloud.ServerMapper;
import com.baiyi.opscloud.service.server.ServerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/24 9:39 上午
 * @Version 1.0
 */
@BusinessType(BusinessTypeEnum.SERVER)
@Service
@RequiredArgsConstructor
public class ServerServiceImpl extends AbstractBusinessService<Server> implements ServerService {

    private final ServerMapper serverMapper;

    @Override
    public Server getById(Integer id) {
        return serverMapper.selectByPrimaryKey(id);
    }

    @Override
    public Server getByPrivateIp(String privateIp) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("privateIp", privateIp);
        return serverMapper.selectOneByExample(example);
    }

    @Override
    public Server getByKey(String key){
        return getByPrivateIp(key);
    }

    @Override
    public BusinessAssetRelationVO.IBusinessAssetRelation toBusinessAssetRelation(DsAssetVO.Asset asset) {
        Server server = getByKey(asset.getAssetKey());
        return BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
    }

    @Override
    public DataTable<Server> queryServerPage(ServerParam.ServerPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Server> data = serverMapper.queryServerByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<Server> queryUserPermissionServerPage(ServerParam.UserPermissionServerPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Server> data = serverMapper.queryUserPermissionServerByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<Server> queryUserRemoteServerPage(ServerParam.UserRemoteServerPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Server> data = serverMapper.queryUserRemoteServerPage(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void add(Server server) {
        server.setId(null);
        serverMapper.insert(server);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void update(Server server) {
        serverMapper.updateByPrimaryKey(server);
    }

    @Override
    public void deleteById(Integer id) {
        serverMapper.deleteByPrimaryKey(id);
    }

    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void delete(Server server) {
        serverMapper.deleteByPrimaryKey(server.getId());
    }

    @Override
    public Server getMaxSerialNumberServer(Integer serverGroupId, Integer envType) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        criteria.andEqualTo("envType", envType);
        example.setOrderByClause("serial_number desc limit 1");
        return serverMapper.selectOneByExample(example);
    }

    @Override
    public List<Server> queryByGroupIdAndEnvType(Integer serverGroupId, Integer envType) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        criteria.andEqualTo("envType", envType);
        return serverMapper.selectByExample(example);
    }

    @Override
    public int countByServerGroupId(Integer serverGroupId) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return serverMapper.selectCountByExample(example);
    }

    @Override
    public List<Server> queryByServerGroupId(Integer serverGroupId) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverGroupId", serverGroupId);
        return serverMapper.selectByExample(example);
    }


}
