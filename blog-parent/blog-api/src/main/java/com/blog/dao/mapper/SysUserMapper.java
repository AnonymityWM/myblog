package com.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.dao.pojo.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser findUser(String account,String password);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);
}
