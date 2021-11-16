package com.blog.service.impl;

import com.blog.dao.mapper.SysUserMapper;
import com.blog.dao.pojo.SysUser;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import com.blog.vo.ErrorCode;
import com.blog.vo.LoginUserVo;
import com.blog.vo.Result;
import com.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        UserVo userVo  = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findUser(String account, String password) {
        return sysUserMapper.findUser(account, password);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1. token合法性校验
         *    是否为空，解析是否成功 redis是否存在
         * 2. 如果校验失败 返回错误
         * 3. 如果成功，返回对应的结果 LoginUserVo
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();

        // 类的转换
        BeanUtils.copyProperties(sysUser,loginUserVo);
        loginUserVo.setId(String.valueOf(sysUser.getId()));

        return Result.success(loginUserVo);
    }

    /**
     * 根据账户account查用户
     * @param account
     * @return
     */
    @Override
    public SysUser findUserByAccount(String account) {
        return sysUserMapper.findUserByAccount(account);
    }

    /**
     * 存储新用户信息
     */
    @Override
    @Transactional
    public void save(SysUser sysUser){
        sysUserMapper.save(sysUser);
    }
}
