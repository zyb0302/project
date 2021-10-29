package com.school.manager.service;

import com.github.pagehelper.PageInfo;
import com.school.manager.model.User;

public interface IUserService {
    PageInfo<User> queryUserList(User result, PageInfo pageInfo);
    void add(User user);
    void update(User user);
    void delete(Integer id);
    User queryByName(String name);
    User queryUserById(Integer id);
    void updateStatus(Integer status,Integer id);
}
