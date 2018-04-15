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
    //交易信息添加的接口
    String ADD_TRADE_RECORD = "/api/trade/add";
    //获取成员用户列表
    String TRADE_INFO_LIST = "/api/trade/list";
    //删除交易信息
    String DEL_TRADE_TRADE = "/api/trade/del";
    //获取指定月份的资产分析
    String TRADE_ANALYSIS_MONTH = "/api/trade/analysis";
}
