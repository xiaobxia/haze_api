package com.vxianjin.gringotts.web.utils;

import com.sun.org.apache.commons.beanutils.BeanUtils;
import com.vxianjin.gringotts.constant.Constant;
import com.vxianjin.gringotts.risk.pojo.RiskManageRule;
import com.vxianjin.gringotts.risk.utils.ConstantRisk;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * 提供读取缓存的方法
 *
 * @author dongyukai 2017-09-28
 * 从back.utils包中迁到web.utils包
 */

public class SysCacheUtils {

    private static ServletContext servletContext = null;

    /**
     * 获取系统参数
     *
     * @return 2014-7-16 fx
     */
    public static LinkedHashMap<String, String> getConfigParams(String type) {
        return (LinkedHashMap<String, String>) getServletContext()
                .getAttribute(type);
    }

    /**
     * 获取系统参数
     *
     * @return 2014-7-16 fx
     */
    public static List<BackConfigParams> getListConfigParams(String type) {
        type = type + Constant.SYS_CONFIG_LIST;
        return (List<BackConfigParams>) getServletContext().getAttribute(type);
    }

    /**
     * 获得Map集合
     *
     * @return
     */
    public static Map<String, String> getConfigMap(String type) {
        return (Map<String, String>) getServletContext().getAttribute(type);
    }

    /**
     * 获得基础规则Map集合
     *
     * @return
     */
    public static Map<String, String> getBaseRule() {
        // 必须这么写，防止外部修改
        Map<String, String> map1 = (Map<String, String>) getServletContext()
                .getAttribute(ConstantRisk.BASE_RULE);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.putAll(map1);
        return map2;
    }

    /**
     * 获得所有规则集合
     *
     * @return
     */
    public static Map<String, RiskManageRule> getAllRule() {
        Map<String, RiskManageRule> map2 = new HashMap<String, RiskManageRule>();
        try {
            // 必须这么写，防止外部修改
            Map<String, RiskManageRule> map1 = (Map<String, RiskManageRule>) getServletContext()
                    .getAttribute(ConstantRisk.ALL_RULE);
            for (String key : map1.keySet()) {
                RiskManageRule riskManageRule = new RiskManageRule();
                BeanUtils.copyProperties(riskManageRule, map1.get(key));
                map2.put(key, riskManageRule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map2;
    }

    /**
     * 弃用,存在引用传递问题9999999
     *
     * @return
     */
    public static List<RiskManageRule> getZrRule() {
        List<RiskManageRule> list2 = new ArrayList<RiskManageRule>();
        try {
            List<RiskManageRule> list1 = (List<RiskManageRule>) getServletContext()
                    .getAttribute(ConstantRisk.RULE_ZR);
            // 复制的时候当前节点基本类型没有问题，但是内部非基本类型，如child的拷贝仍然会被修改
            Map<String, RiskManageRule> map1 = (Map<String, RiskManageRule>) getServletContext()
                    .getAttribute(ConstantRisk.ALL_RULE);
            for (RiskManageRule riskManageRule : list1) {
                RiskManageRule riskManageRule2 = new RiskManageRule();
                BeanUtils.copyProperties(riskManageRule2, riskManageRule);
                list2.add(riskManageRule2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list2;
    }

    public static ServletContext getServletContext() {
        if (servletContext == null) {
            servletContext = ContextLoader.getCurrentWebApplicationContext()
                    .getServletContext();
        }
        return servletContext;
    }

}
