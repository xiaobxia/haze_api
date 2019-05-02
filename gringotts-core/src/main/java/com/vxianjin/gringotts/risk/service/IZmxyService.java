package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.common.ResponseContent;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


public interface IZmxyService {
    /**
     * 获得芝麻信用的openID，该接口免费，强烈建议建议调用芝麻信用相关信息之前先调用该接口以获得正确的openId
     *
     * @param params 必要参数：<br>
     *               userName用户姓名<br>
     *               cardNum用户身份证号码<br>
     *               userId用户主键ID<br>
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到芝麻，但未被正常解析<br>
     * 仅当code=200时，msg是获得的openId<br>
     * 100时是用户为授权，需要调用getURL方法进行授权<br>
     */
    ResponseContent getOpenId(HashMap<String, Object> params);

    /**
     * 获得芝麻信用授权URL，该接口免费，
     *
     * @param params 必要参数：<br>
     *               userName用户姓名<br>
     *               cardNum用户身份证号码<br>
     *               userId用户主键ID<br>
     *               request用于判断客户端类型
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 仅当code=200时，msg是获得的授权URL，调用者需要通过页面打开这个链接完成授权<br>
     */
    ResponseContent getURL(HashMap<String, Object> params,
                           HttpServletRequest request);

    /**
     * 获得芝麻分，该接口收费
     *
     * @param params 必要参数：<br>
     *               openId芝麻信用返回的openId<br>
     *               userId用户主键ID<br>
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到芝麻，但未被正常解析<br>
     * 仅当code=200时，msg是获得的芝麻分<br>
     */
    ResponseContent getZmScore(HashMap<String, Object> params);

    /**
     * 获得芝麻行业关注度，该接口收费
     *
     * @param params 必要参数：<br>
     *               openId芝麻信用返回的openId<br>
     *               userId用户主键ID<br>
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到芝麻，但未被正常解析<br>
     * 仅当code=200时，msg是获得的芝麻信息<br>
     */
    ResponseContent getCreditWatchList(HashMap<String, Object> params);

}
