package com.imooc.mapper;

import com.imooc.pojo.ChatRecord;
import com.imooc.utils.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRecordMapper extends MyMapper<ChatRecord> {

    /**
     * 批量更新消息记录表(签收消息)
     * @param msgIdList 消息编号集合
     */
    void batchUpdateMsgSigned(List<String> msgIdList);

}