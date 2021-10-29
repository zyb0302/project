package com.school.manager.service;

import java.util.List;
import com.github.pagehelper.PageInfo;
import com.school.manager.model.MerItems;

public interface IMerItemsService {
    PageInfo<MerItems> queryMerItemsList(MerItems model, PageInfo pageInfo);
    void addMerItems(MerItems model);
    void updateMerItems(MerItems model);
    void deleteMerItems(Integer id);
    MerItems queryMerItemsById(Integer id);
    List<MerItems> queryMerItemsAll();
    void updateWaiting(Integer status,Integer id);

    List<MerItems> queryMerItemsByUserIdAndWaiting(String userId);
}
