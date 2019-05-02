package com.vxianjin.gringotts.common;


import com.google.gson.Gson;

/**
 * 业务内json消息通用通信
 *
 * @author tangy
 * @version [版本号, 2018年1月23日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JsonResult {
    /**
     * 结果SUCCESS FAILURE
     */
    private String result;
    /**
     * 通知消息 成功
     */
    private String msg;

    /**
     * 结果数量 1
     */
    private String count;

    /**
     * 结果代码 类似000000
     */
    private String code;

    /**
     * 传的json对象转化串  {}
     */
    private Object data;

    public JsonResult() {
        this.count = "";
        this.data = "";
    }

    public JsonResult(String result, String msg, String count, Object obj) {
        this.result = result;
        this.msg = msg;
        this.count = ((count == null) || ("0".equals(count)) ? "" : count);

        if (obj == null) {
            this.data = "";
        } else {
            this.data = obj;
        }
        this.code = "";
    }

    public JsonResult(String result, String msg, String count, Object obj, String code) {
        this.result = result;
        this.msg = msg;
        this.count = (count == null) ? "" : count;

        if (obj == null) {
            this.data = "";
        } else {
            this.data = obj;
        }
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("result:").append(result).append(" msg:").append(
                msg).append(" code:").append(code).append(" count:").append(count).append(
                " data:").append(data);

        return sb.toString();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
