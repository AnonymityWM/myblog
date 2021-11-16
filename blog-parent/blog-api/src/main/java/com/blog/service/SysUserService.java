package com.blog.service;

import com.blog.dao.pojo.SysUser;
import com.blog.vo.Result;
import com.blog.vo.UserVo;

public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findUser(String account,String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    /**
     * 存储新用户信息
     */
    void save(SysUser sysUser);
}
