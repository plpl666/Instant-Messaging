package com.imooc.mapper;

import com.imooc.pojo.Users;
import com.imooc.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper extends MyMapper<Users> {

    /**
     * 从账号字典中获得账号
     * @return 用户账号
     */
    String getUsername();

    /**
     * 删除账号字典中的账号
     * @param username 用户账号
     */
    void deleteUsername(String username);

}