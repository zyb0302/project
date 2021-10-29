package com.school.manager.service.impl;

import com.school.manager.common.ResultType;
import com.school.manager.exception.BaseDataException;
import com.school.manager.mapper.MarketConfirmMapper;
import com.school.manager.model.User;
import com.school.manager.service.IMarketConfirmService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.manager.model.MarketConfirm;
import com.school.manager.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MarketConfirmServiceImpl implements IMarketConfirmService {

    @Autowired
    private MarketConfirmMapper MarketConfirmMapper;

    @Override
    public PageInfo<MarketConfirm> queryMarketConfirmList(MarketConfirm model, PageInfo pageInfo) {
        PageInfo<MarketConfirm> MarketConfirmPageInfo;
        try {
            Integer currentPage = pageInfo.getPageNum();
            Integer pageSize = pageInfo.getPageSize();
            Example example = new Example(MarketConfirm.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(model.getPhoneid())) {
                criteria.andEqualTo("phoneid",model.getPhoneid());
            }
            String userName = UserUtils.getUserName();
            Integer userId = UserUtils.getUserId();
            if (!"admin".equals(userName)) {
                criteria.andEqualTo("userid",userId);
            }
            PageHelper.startPage(currentPage, pageSize);
            List<MarketConfirm> list = MarketConfirmMapper.selectByExample(example);
            MarketConfirmPageInfo = new PageInfo<>(list);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(queryMarketConfirmList) " + e);
            throw e;
        }
        return MarketConfirmPageInfo;
    }

    @Override
    public void addMarketConfirm(MarketConfirm model) {
        try {
            MarketConfirm marketConfirm = queryByPhoneId(model.getPhoneid());
            if (marketConfirm != null) {
                throw new BaseDataException(ResultType.SUCCESS);
            }
            model.setIsconfirm("false");
            model.setMarketsource("mercari");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.setCreatetime(sdf.format(new Date()));
            MarketConfirmMapper.insert(model);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(addMarketConfirm) " + e);
            throw e;
        }
    }

    private MarketConfirm queryByPhoneId (String phoneId) {
        try {
            Example example = new Example(MarketConfirm.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("phoneid",phoneId);
            return MarketConfirmMapper.selectOneByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(queryMarketConfirmList) " + e);
            throw e;
        }
    }
    @Override
    public void updateMarketConfirm(MarketConfirm model) {
        try {
            MarketConfirmMapper.updateByPrimaryKeySelective(model);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(updateMarketConfirm) " + e);
            throw e;
        }
    }

    @Override
    public void deleteMarketConfirm(Integer id) {
        try {
            MarketConfirmMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(deleteMarketConfirm) " + e);
            throw e;
        }
    }

    @Override
    public MarketConfirm queryMarketConfirmById(Integer id) {
        MarketConfirm MarketConfirm = null;
        try {
            MarketConfirm = MarketConfirmMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(queryMarketConfirmById) " + e);
            throw e;
        }
        return MarketConfirm;
    }

    @Override
    public List<MarketConfirm> queryMarketConfirmAll() {
        List<MarketConfirm> list = null;
        try {
            list = MarketConfirmMapper.selectAll();
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(queryMarketConfirmAll) " + e);
            throw e;
        }
        return list;
    }

    @Override
    public MarketConfirm queryMarketConfirmByUserIdAndPhoneId(String userId,String phoneId) {
        try {
            Example example = new Example(MarketConfirm.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userid",userId);
            criteria.andEqualTo("phoneid",phoneId);
            return MarketConfirmMapper.selectOneByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MarketConfirmService error at func(queryMarketConfirmList) " + e);
            throw e;
        }
    }

    @Override
    public void updateIsconfirm(String isconfirm,Integer id) {
        MarketConfirm marketConfirm = MarketConfirmMapper.selectByPrimaryKey(id);
        if (marketConfirm != null) {
            if ("false".equals(isconfirm)) {
                marketConfirm.setIsconfirm("true");
            }else {
                marketConfirm.setIsconfirm("false");
            }
            MarketConfirmMapper.updateByPrimaryKeySelective(marketConfirm);
        }
    }
}
