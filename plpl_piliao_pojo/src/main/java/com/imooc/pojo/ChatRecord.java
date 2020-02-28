package com.imooc.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "chat_record")
public class ChatRecord {
    /**
     * 聊天记录编号
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
     * 消息内容
     */
    private String message;

    /**
     * 语音消息路径
     */
    @Column(name = "audio_url")
    private String audioUrl;

    /**
     * 用户消息签收(是否已收到消息)
     */
    @Column(name = "sign_flag")
    private Integer signFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取聊天记录编号
     *
     * @return id - 聊天记录编号
     */
    public String getId() {
        return id;
    }

    /**
     * 设置聊天记录编号
     *
     * @param id 聊天记录编号
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
     * 获取消息内容
     *
     * @return message - 消息内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息内容
     *
     * @param message 消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取语音消息路径
     *
     * @return message - 语音消息路径
     */
    public String getAudioUrl() {
        return audioUrl;
    }

    /**
     * 设置语音消息路径
     *
     * @param audioUrl 语音消息路径
     */
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    /**
     * 获取用户消息签收(是否已收到消息)
     *
     * @return sign_flag - 用户消息签收(是否已收到消息)
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 设置用户消息签收(是否已收到消息)
     *
     * @param signFlag 用户消息签收(是否已收到消息)
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}