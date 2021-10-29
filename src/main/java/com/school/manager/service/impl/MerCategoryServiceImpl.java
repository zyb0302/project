package com.school.manager.service.impl;

import com.school.manager.mapper.MerCategoryMapper;
import com.school.manager.service.IMerCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.manager.model.MerCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MerCategoryServiceImpl implements IMerCategoryService {

    @Autowired
    private MerCategoryMapper MerCategoryMapper;

    @Override
    public PageInfo<MerCategory> queryMerCategoryList(MerCategory model, PageInfo pageInfo) {
        PageInfo<MerCategory> MerCategoryPageInfo;
        try {
            Integer currentPage = pageInfo.getPageNum();
            Integer pageSize = pageInfo.getPageSize();
            Example example = new Example(MerCategory.class);
            Example.Criteria criteria = example.createCriteria();
            PageHelper.startPage(currentPage, pageSize);
            List<MerCategory> list = MerCategoryMapper.selectByExample(example);
            MerCategoryPageInfo = new PageInfo<>(list);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(queryMerCategoryList) " + e);
            throw e;
        }
        return MerCategoryPageInfo;
    }

    @Override
    public void addMerCategory(MerCategory model) {
        try {
            MerCategoryMapper.insert(model);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(addMerCategory) " + e);
            throw e;
        }
    }

    @Override
    public void updateMerCategory(MerCategory model) {
        try {
            MerCategoryMapper.updateByPrimaryKeySelective(model);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(updateMerCategory) " + e);
            throw e;
        }
    }

    @Override
    public void deleteMerCategory(Integer id) {
        try {
            MerCategoryMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(deleteMerCategory) " + e);
            throw e;
        }
    }

    @Override
    public MerCategory queryMerCategoryById(Integer id) {
        MerCategory MerCategory = null;
        try {
            MerCategory = MerCategoryMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(queryMerCategoryById) " + e);
            throw e;
        }
        return MerCategory;
    }

    @Override
    public List<MerCategory> queryMerCategoryAll() {
        List<MerCategory> list = null;
        try {
            list = MerCategoryMapper.selectAll();
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(queryMerCategoryAll) " + e);
            throw e;
        }
        return list;
    }

    @Override
    public List<MerCategory> queryByParentIdAndLevel(Integer parentId,Integer level) {
        List<MerCategory> list;
        try {
            Example example = new Example(MerCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",parentId);
            criteria.andEqualTo("level",level);
            list = MerCategoryMapper.selectByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(queryMerCategoryAll) " + e);
            throw e;
        }
        return list;
    }

    @Override
    public MerCategory queryByCategorydAndLevel(Integer categoryId,Integer level) {
        List<MerCategory> list;
        try {
            Example example = new Example(MerCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("categoryId",categoryId);
            criteria.andEqualTo("level",level);
            return MerCategoryMapper.selectOneByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(queryMerCategoryAll) " + e);
            throw e;
        }
    }

    @Override
    public void deleteAll() {
        try {
            Example example = new Example(MerCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andNotEqualTo("categoryId",0);
            MerCategoryMapper.deleteByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerCategoryService error at func(queryMerCategoryAll) " + e);
            throw e;
        }
    }
}
