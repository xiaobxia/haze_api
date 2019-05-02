package com.vxianjin.gringotts.web.pojo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: chenkai
 * @Date: 2018/6/29 14:06
 * @Description:
 */
public class HeartAlive implements Serializable {

    private static final long serialVersionUID = 8386837100299491712L;

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "===>link....";
    }
}
