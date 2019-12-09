package com.imooc.ad.dao;

import com.imooc.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 14:25
 * @Description:广告创意JPADAO
 */
public interface CreativeRepository extends JpaRepository<Creative,Long> {

}
