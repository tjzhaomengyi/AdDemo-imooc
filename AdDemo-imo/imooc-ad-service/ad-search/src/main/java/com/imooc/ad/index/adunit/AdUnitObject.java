package com.imooc.ad.index.adunit;

import com.imooc.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/11 17:16
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject; //为了方便获取推广计划，增加一个推广计划的引用


    void update(AdUnitObject newAdUnitObject){
        if (null != newAdUnitObject.getUnitId()) {
            this.unitId = newAdUnitObject.getUnitId();
        }
        if (null != newAdUnitObject.getPlanId()) {
            this.planId = newAdUnitObject.getPlanId();
        }
        if (null != newAdUnitObject.getPositionType()) {
            this.positionType = newAdUnitObject.getPositionType();
        }
        if (null != newAdUnitObject.getUnitStatus()) {
            this.unitStatus = newAdUnitObject.getUnitStatus();
        }
        if (null != newAdUnitObject.getAdPlanObject()) {
            this.adPlanObject = newAdUnitObject.getAdPlanObject();
        }
    }

    //校验positionType类型
    public static boolean isKaiping(int positionType){
        return (positionType & AdUnitContants.POSITION_TYPE.KAIPING) > 0;
    }

    public static boolean isTiepian(int positionType){
        return (positionType & AdUnitContants.POSITION_TYPE.TIEPIAN) > 0;
    }

    public static boolean isTiepianMiddle(int positionType){
        return (positionType & AdUnitContants.POSITION_TYPE.TIEPIAN_MIDDLE) > 0;
    }

    public static boolean isTiepianPause(int positionType){
        return (positionType & AdUnitContants.POSITION_TYPE.TIEPIAN_PAUSE) > 0;
    }

    public static boolean isTiepianPost(int positionType){
        return (positionType & AdUnitContants.POSITION_TYPE.TIEPIAN_POST) > 0;
    }

    //todo:这有啥用判断adSlotTYpe，是否跟positionType一致？这个直接判断postionType不就完了？？这个adSlotType是谁的属性？？？
    //todo:这个传进来的类型直接等于比较就可以，这个多此一举了
    public static boolean isAdSlotTypeOk(int adSlotType,int positionType){
        switch (adSlotType){
            case AdUnitContants.POSITION_TYPE.KAIPING:
                return isKaiping(positionType);
            case AdUnitContants.POSITION_TYPE.TIEPIAN:
                return isTiepian(positionType);
            case AdUnitContants.POSITION_TYPE.TIEPIAN_MIDDLE:
                return isTiepianMiddle(positionType);
            case AdUnitContants.POSITION_TYPE.TIEPIAN_PAUSE:
                return isTiepianPause(positionType);
            case AdUnitContants.POSITION_TYPE.TIEPIAN_POST:
                return isTiepianPost(positionType);
            default:
                return false;

        }
    }
}
