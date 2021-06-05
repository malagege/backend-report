package com.example.bean.response.impl;

import com.example.bean.response.Response;

public class NormalResponse  implements Response{
    private boolean success;
    private String msg;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}
