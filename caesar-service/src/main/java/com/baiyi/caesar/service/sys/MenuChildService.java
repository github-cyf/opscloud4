package com.baiyi.caesar.service.sys;

import com.baiyi.caesar.domain.generator.caesar.MenuChild;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/1 5:10 下午
 * @Version 1.0
 */
public interface MenuChildService {

    void add(MenuChild menuChild);

    void update(MenuChild menuChild);

    void del(Integer id);

    MenuChild getById(Integer id);

    List<MenuChild> listByMenuId(Integer menuId);
}
