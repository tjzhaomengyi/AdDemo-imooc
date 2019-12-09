package com.imooc.ad.service;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.request.CreateUserRequest;
import com.imooc.ad.vo.response.CreateUserResponse;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:52
 * @Description:
 */
public interface IUserService {

    /**
     * 创建用户
     * @param request
     * @return
     * @throws AdException
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdException;
}
