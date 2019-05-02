package com.vxianjin.gringotts.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 输出数据给app
 *
 * @author gaoyuhai
 * 2016-11-21 下午12:16:27
 */
//@Deprecated
public class WriteUtil {

    public static void writeDataToApp(HttpServletResponse response, String data) {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
