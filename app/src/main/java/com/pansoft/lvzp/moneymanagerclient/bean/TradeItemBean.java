package com.pansoft.lvzp.moneymanagerclient.bean;

/**
 * Created by lv_zhp on 2018/4/6.
 */
public class TradeItemBean {

    private String userOid;//成员用户对oid
    private int type;//0是支出 1是收入
    private String date;//时间
    private String consumeTag;//消费的Tag
    private String remarks;//备注信息
    private String money;//金额

    public String getConsumeTag() {
        return consumeTag;
    }

    public void setConsumeTag(String consumeTag) {
        this.consumeTag = consumeTag;
    }

    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
