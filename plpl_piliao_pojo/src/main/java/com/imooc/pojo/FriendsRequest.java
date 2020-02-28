package com.imooc.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "friends_request")
public class FriendsRequest {
    /**
     * 编号
     */
    @Id
    private String id;

    /**
     * 发送者编号
     */
    @Column(name = "send_user_id")
    private String sendUserId;

    /**
     * 接收者编号
     */
    @Column(name = "receive_user_id")
    private String receiveUserId;

    /**
     * 请求时间
     */
    @Column(name = "request_date_time")
    private Date requestDateTime;

    /**
     * 获取编号
     *
     * @return id - 编号
     */
    public String getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取发送者编号
     *
     * @return send_user_id - 发送者编号
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置发送者编号
     *
     * @param sendUserId 发送者编号
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取接收者编号
     *
     * @return receive_user_id - 接收者编号
     */
    public String getReceiveUserId() {
        return receiveUserId;
    }

    /**
     * 设置接收者编号
     *
     * @param receiveUserId 接收者编号
     */
    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**
     * 获取请求时间
     *
     * @return request_date_time - 请求时间
     */
    public Date getRequestDateTime() {
        return requestDateTime;
    }

    /**
     * 设置请求时间
     *
     * @param requestDateTime 请求时间
     */
    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }
}