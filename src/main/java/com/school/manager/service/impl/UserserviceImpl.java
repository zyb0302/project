package com.school.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.school.manager.common.ResultType;
import com.school.manager.exception.BaseDataException;
import com.school.manager.mapper.UserMapper;
import com.school.manager.model.User;
import com.school.manager.service.IUserService;
import com.school.manager.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserserviceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo<User> queryUserList(User user, PageInfo pageInfo) {
        PageInfo<User> carPageInfo;
        try {
            Integer currentPage = pageInfo.getPageNum();
            Integer pageSize = pageInfo.getPageSize();
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(user.getUsername())) {
                criteria.andLike("username", "%"+user.getUsername()+"%");
            }
            PageHelper.startPage(currentPage, pageSize);
            List<User> users = userMapper.selectByExample(example);
            carPageInfo = new PageInfo<>(users);
        } catch (Exception e) {
            System.out.println(new Timestamp(System.currentTimeMillis()) + "UserService error at func(queryUserList) " + e);
            throw e;
        }
        return carPageInfo;
    }

    @Override
    public void add(User user) {
        try {
            user.setStatus(1);
            user.setRole(3);
            userMapper.insert(user);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void delete(Integer id) {
        try {
            userMapper.deleteByPrimaryKey(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User queryByName(String name) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        try {
            if (StringUtils.isNotBlank(name)) {
                criteria.andEqualTo("username",name);
                User user = userMapper.selectOneByExample(example);
                return user;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User queryUserById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public void updateStatus(Integer status,Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user != null) {
            if (status == 1) {
                user.setStatus(2);
            }else {
                user.setStatus(1);
            }
            userMapper.updateByPrimaryKeySelective(user);
        }
    }
}
