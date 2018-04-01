package com.pansoft.lvzp.moneymanagerclient.bean;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public class MemberUserBean {

    private String oid;
    private String userNo;
    private String userName;
    private String userPhone;
    private String parentOid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }



    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getParentOid() {
        return parentOid;
    }

    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }
}
