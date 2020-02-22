package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcBusinessTag;
import com.baiyi.opscloud.domain.vo.tag.OcBusinessTagVO;
import tk.mybatis.mapper.common.Mapper;

public interface OcBusinessTagMapper extends Mapper<OcBusinessTag> {

    OcBusinessTag queryOcBusinessTagByUniqueKey(OcBusinessTagVO.BusinessTag businessTag);

    int deleteOcBusinessTagByUniqueKey(OcBusinessTagVO.BusinessTag businessTag);
}