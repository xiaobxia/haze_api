package com.vxianjin.gringotts.web.listener;

import javax.servlet.ServletContext;

/**
 * 类描述：缓存接口类 <br>
 * 创建人：fanyinchuan<br>
 * 创建时间：2016-6-28 下午08:15:05 <br>
 */
public interface Starter {
    void init(ServletContext ctx);

    /**
     * 刷新风控相关信息
     *
     * @param ctx
     */
    void risk(ServletContext ctx);
}
