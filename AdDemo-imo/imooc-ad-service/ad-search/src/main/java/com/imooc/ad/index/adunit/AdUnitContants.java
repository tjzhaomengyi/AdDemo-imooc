package com.imooc.ad.index.adunit;

/**
 * @Auther: zhaomengyi
 * @Date: 2019/12/18 17:32
 * @Description:
 */
public class AdUnitContants {

    /**
     * 广告位类型,定义为2的倍数，加快索引速度
     */
    public static class POSITION_TYPE {
        public static final int KAIPING = 1;//开屏
        public static final int TIEPIAN = 2;//贴片
        public static final int TIEPIAN_MIDDLE = 4; //中部贴片
        public static final int TIEPIAN_PAUSE = 8; //暂停贴片
        public static final int TIEPIAN_POST = 16; //尾部贴片
    }
}
