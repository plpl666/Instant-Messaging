package com.imooc.mapper;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.FriendRequestVO;
import com.imooc.pojo.vo.MyFriendsVO;
import com.imooc.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersExpandMapper extends MyMapper<Users> {

    /**
     * 获取用户好友请求列表
     * @param id 用户编号
     * @return 用户请求对象集合
     */
    List<FriendRequestVO> queryFriendRequestList(String id);

    /**
     * 获取用户好友列表
     * @param id 用户编号
     * @return 好友对象集合
     */
    List<MyFriendsVO> queryMyFriends(String id);

}