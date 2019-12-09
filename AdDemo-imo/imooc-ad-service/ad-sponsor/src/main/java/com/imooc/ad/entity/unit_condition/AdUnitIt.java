package com.imooc.ad.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/9 12:22
 * @Description:兴趣爱好限制
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_unit_it")
public class AdUnitIt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Basic
    @Column(name = "unit_id",nullable = false)
    private Long unitId;

    @Basic
    @Column(name = "it_tag",nullable = false)
    private String itTag;

    public AdUnitIt(Long unitId,String itTag){
        this.unitId = unitId;
        this.itTag = itTag;
    }

}
