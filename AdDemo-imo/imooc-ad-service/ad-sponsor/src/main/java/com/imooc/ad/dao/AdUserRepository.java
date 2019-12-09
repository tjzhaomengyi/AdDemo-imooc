package com.imooc.ad.dao;

import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 12:32
 * @Description:AdUser的JPADAO
 */
public interface AdUserRepository extends JpaRepository<AdUser,Long> {

    /**
     * 根据用户名查找用户记录
     * @param username
     * @return
     */
    AdUser findByUsername(String username);

}
