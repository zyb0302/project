package com.school.manager.mapper;

import com.school.manager.model.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
@Component
public interface UserMapper extends Mapper<User> {
}
