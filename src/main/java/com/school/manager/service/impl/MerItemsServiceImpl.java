package com.school.manager.service.impl;

import com.school.manager.mapper.MerItemsMapper;
import com.school.manager.model.MarketConfirm;
import com.school.manager.model.MerCategory;
import com.school.manager.model.User;
import com.school.manager.service.IMerCategoryService;
import com.school.manager.service.IMerItemsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.manager.model.MerItems;
import com.school.manager.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MerItemsServiceImpl implements IMerItemsService {

    @Autowired
    private MerItemsMapper MerItemsMapper;
    
    @Autowired
    private IMerCategoryService merCategoryService;

    @Override
    public PageInfo<MerItems> queryMerItemsList(MerItems model, PageInfo pageInfo) {
        PageInfo<MerItems> MerItemsPageInfo;
        try {
            Integer currentPage = pageInfo.getPageNum();
            Integer pageSize = pageInfo.getPageSize();
            Example example = new Example(MerItems.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(model.getItemname())) {
                criteria.andLike("itemname","%"+model.getItemname()+"%");
            }
            if (StringUtils.isNotBlank(model.getMark())) {
                criteria.andLike("mark","%"+model.getMark()+"%");
            }
            String userName = UserUtils.getUserName();
            Integer userId = UserUtils.getUserId();
            if (!"admin".equals(userName)) {
                criteria.andEqualTo("userid",userId);
            }
            PageHelper.startPage(currentPage, pageSize);
            example.setOrderByClause("createtime desc");
            List<MerItems> list = MerItemsMapper.selectByExample(example);
            list.forEach(item -> {
                if (StringUtils.isNotBlank(item.getImages())) {
                    String[] split = item.getImages().split(",");
                    item.setFirstImage(split[0]);
                }
                MerCategory merCategorie = merCategoryService.queryByCategorydAndLevel(item.getT1CategoryId(), 1);
                if (merCategorie != null) {
                    item.setT1CategoryName(merCategorie.getCategoryName());
                }
            });
            MerItemsPageInfo = new PageInfo<>(list);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(queryMerItemsList) " + e);
            throw e;
        }
        return MerItemsPageInfo;
    }

    @Override
    public void addMerItems(MerItems model) {
        try {
            /*HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            Object filenames = session.getAttribute("imgpath");
            if (filenames != null) {
                model.setImages(filenames.toString());
            }*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.setCreatetime(sdf.format(new Date()));
            if (StringUtils.isNotBlank(model.getSize1())) {
                model.setSize(model.getSize1());
            }
            if (StringUtils.isNotBlank(model.getSize2())) {
                model.setSize(model.getSize2());
            }
            if (StringUtils.isNotBlank(model.getSize3())) {
                model.setSize(model.getSize3());
            }
            List<MerItems> merItems = queryListOrderBy();
            if (merItems != null && merItems.size() > 0) {
                String sku = merItems.get(0).getSku();
                sku = sku.substring(sku.indexOf("a")+1);
                Integer s = Integer.parseInt(sku)+1;
                model.setSku("a"+s);
            }else {
                model.setSku("a1001001");
            }
            model.setMark("mercari");
            model.setUserid(UserUtils.getUserId());
            //model.setImages(model.getFile());
            model.setWaiting(1);
            model.setUtimes(1);
            MerItemsMapper.insert(model);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(addMerItems) " + e);
            throw e;
        }
    }
    private List<MerItems> queryListOrderBy(){
        try {
            Example example = new Example(MerItems.class);
            Example.Criteria criteria = example.createCriteria();
            example.setOrderByClause("sku desc");
            return MerItemsMapper.selectByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(queryListOrderBy) " + e);
            throw e;
        }
    }
    @Override
    public void updateMerItems(MerItems model) {
        try {
            MerItems merItems = queryMerItemsById(model.getId());
            if (merItems != null) {
                model.setUtimes(merItems.getUtimes()+1);
            }
            if (StringUtils.isNotBlank(model.getSize1())) {
                model.setSize(model.getSize1());
            }
            if (StringUtils.isNotBlank(model.getSize2())) {
                model.setSize(model.getSize2());
            }
            if (StringUtils.isNotBlank(model.getSize3())) {
                model.setSize(model.getSize3());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            model.setUpdatetime(sdf.format(new Date()));
            //model.setImages(model.getFile());
            MerItemsMapper.updateByPrimaryKeySelective(model);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(updateMerItems) " + e);
            throw e;
        }
    }

    @Override
    public void deleteMerItems(Integer id) {
        try {
            MerItemsMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(deleteMerItems) " + e);
            throw e;
        }
    }

    @Override
    public MerItems queryMerItemsById(Integer id) {
        MerItems MerItems = null;
        try {
            MerItems = MerItemsMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(queryMerItemsById) " + e);
            throw e;
        }
        return MerItems;
    }

    @Override
    public List<MerItems> queryMerItemsAll() {
        List<MerItems> list = null;
        try {
            list = MerItemsMapper.selectAll();
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(queryMerItemsAll) " + e);
            throw e;
        }
        return list;
    }

    @Override
    public void updateWaiting(Integer status,Integer id) {
        MerItems merItems = MerItemsMapper.selectByPrimaryKey(id);
        if (merItems != null) {
            if (status == 1) {
                merItems.setWaiting(2);
            }else {
                merItems.setWaiting(1);
            }
            MerItemsMapper.updateByPrimaryKeySelective(merItems);
        }
    }

    @Override
    public List<MerItems> queryMerItemsByUserIdAndWaiting(String userId) {
        try {
            Example example = new Example(MerItems.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userid",userId);
            criteria.andEqualTo("waiting",1);
            example.setOrderByClause("createtime desc");
            return MerItemsMapper.selectByExample(example);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "MerItemsService error at func(queryMerItemsByUserIdAndWaiting) " + e);
            throw e;
        }
    }
}
