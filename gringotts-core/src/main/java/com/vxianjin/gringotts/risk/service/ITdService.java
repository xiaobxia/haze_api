package com.vxianjin.gringotts.risk.service;

import com.vxianjin.gringotts.common.ResponseContent;

import java.util.HashMap;

public interface ITdService {
    /**
     * 获得同盾贷前审核服务数据，该接口收费
     *
     * @param params 必要参数：<br>
     *               userName用户姓名<br>
     *               cardNum用户身份证号码<br>
     *               userPhone用户手机号码<br>
     *               userId用户主键ID<br>
     * @return 500是是服务器异常，本地代码错误或者连接同盾异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到同盾，但返回错误信息<br>
     * 仅当code=200时是报告生成成功<br>
     * 100是同盾返回空或请求错误<br>
     */
    ResponseContent getReport(HashMap<String, Object> params);

    /**
     * 获得同盾贷前审核报告，该接口收费
     *
     * @param params 必要参数：<br>
     *               reportId报告标识<br>
     *               userId用户主键ID<br>
     * @return 500是是服务器异常，本地代码错误或者连接芝麻信用异常<br>
     * 400是必要参数缺失<br>
     * 300是请求成功发送到同盾，但未被正常解析<br>
     * 仅当code=200时，msg是同盾详情,解析规则参见AESUtil类中的getTdReport<br>
     * 203是同盾返回报告未生成，考虑等待三秒再次发送<br>
     * 100是同盾返回空或请求错误<br>
     */
    ResponseContent queryReport(HashMap<String, Object> params);

    /**
     * 获取同盾手机运营商报告
     *
     * @param params
     * @return
     */
    ResponseContent getMobileReport(HashMap<String, Object> params);
}
