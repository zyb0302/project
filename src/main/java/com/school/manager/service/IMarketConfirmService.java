package com.school.manager.service;

import java.util.List;
import com.github.pagehelper.PageInfo;
import com.school.manager.model.MarketConfirm;

public interface IMarketConfirmService {
    PageInfo<MarketConfirm> queryMarketConfirmList(MarketConfirm model, PageInfo pageInfo);
    void addMarketConfirm(MarketConfirm model);
    void updateMarketConfirm(MarketConfirm model);
    void deleteMarketConfirm(Integer id);
    MarketConfirm queryMarketConfirmById(Integer id);
    List<MarketConfirm> queryMarketConfirmAll();
    MarketConfirm queryMarketConfirmByUserIdAndPhoneId(String userId,String phoneId);
    void updateIsconfirm(String isconfirm,Integer id);
}
