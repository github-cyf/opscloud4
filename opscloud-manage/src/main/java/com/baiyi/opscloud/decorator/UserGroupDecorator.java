package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcUser;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 8:04 下午
 * @Version 1.0
 */
@Component("UserGroupDecorator")
public class UserGroupDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private GroupRepo groupRepo;

    public OcUserGroupVO.UserGroup decorator(OcUserGroupVO.UserGroup userGroup) {
        List<String> usernameList = groupRepo.queryGroupMember(userGroup.getName());
        //Map<String, OcUserVO.User> userMap = Maps.newHashMap();
        List<OcUserVO.User> users = Lists.newArrayList();
        for (String username : usernameList) {
            OcUser ocUser = ocUserService.queryOcUserByUsername(username);
            if (ocUser != null)
                users.add(BeanCopierUtils.copyProperties(ocUser, OcUserVO.User.class));
        }
        userGroup.setUsers(users);
        return userGroup;
    }
}
