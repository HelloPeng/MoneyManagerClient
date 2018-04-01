package com.pansoft.lvzp.moneymanagerclient.http;

/**
 * 作者：吕振鹏
 * 创建时间：09月13日
 * 时间：11:49
 * 版本：v1.0.0
 * 类描述：
 * 修改时间：
 */

public class HttpResult<T> {

    private String message;
    private int status;
    private long timestamp;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
}
