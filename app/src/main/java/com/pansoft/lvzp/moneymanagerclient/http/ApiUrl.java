package com.pansoft.lvzp.moneymanagerclient.http;

/**
 * Created by lv_zhp on 2018/4/1.
 */

public interface ApiUrl {
    //用于验证ip是否可正常使用的接口
    String BASE_CHECK = "/api/base/check";
    //登录的接口
    String LOGIN = "/api/user/login";
    //注册的接口
    String REGISTER = "/api/user/register";
    //添加成员用户的接口
    String ADD_MEMBER_USER = "/api/user/member/add";
    //获取成员用户列表
    String MEMBER_USER_LIST = "/api/user/member/list";

}
