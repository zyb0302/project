package com.school.manager.service;

import java.util.List;
import com.github.pagehelper.PageInfo;
import com.school.manager.model.MerCategory;

public interface IMerCategoryService {
    PageInfo<MerCategory> queryMerCategoryList(MerCategory model, PageInfo pageInfo);
    void addMerCategory(MerCategory model);
    void updateMerCategory(MerCategory model);
    void deleteMerCategory(Integer id);
    MerCategory queryMerCategoryById(Integer id);
    List<MerCategory> queryMerCategoryAll();
    List<MerCategory> queryByParentIdAndLevel(Integer parentId,Integer level);
    MerCategory queryByCategorydAndLevel(Integer categoryId,Integer level);
    void deleteAll();
}
