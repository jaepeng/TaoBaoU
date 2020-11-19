package com.example.taobaou.model.domain;

public interface ILinerItemInfo extends IBaseInfo{
    /**
     * 获取最终价格
     * @return
     */
    String getfinalPrice();

    /**
     * 获取优惠额度
     * @return
     */
    long getCouponAmmount();

    /**
     * 销量
     * @return
     */
    long getVolume();

}
