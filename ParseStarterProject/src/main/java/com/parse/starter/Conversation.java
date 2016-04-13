package com.parse.starter;

import java.util.Date;

/**
 * Created by Vinay on 11/22/2015.
 */
public class Conversation {
    public static final int STATUS_SENDING =0;
    public static final int STATUS_SENT =1;
    public static final int STATUS_FAILED =2;
    private int status = STATUS_SENT;
    private String msg;
    private Date date;
    private String sender;

    public Conversation(String msg, Date date, String sender) {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }

    public Conversation() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public boolean isSent(){

        return  UserList.user.getUsername().equals(sender);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
