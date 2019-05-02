/**
 * <p>Title: IYouGetMoneyMeRepayDao.java</p>
 *
 * @Package com.vxianjin.gringotts.web.dao
 * <p>Description: 你借钱我来还活动数据访问层接口</p>
 * <p>Company:现金侠</p>
 * @author lixingxing
 * @version V1.0
 * @since 2017年3月13日 下午5:15:39
 */
package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.YouGetMoneyMeRepayInfo;

import java.util.List;

/**
 * <p>Description: 你借钱我来还活动数据访问层接口</p>
 * <p>Company:现金侠</p>
 *
 * @author lixingxing
 * @version V1.0
 */
public interface IYouGetMoneyMeRepayDao {

    /**
     * <p>Description:根据免单时间查询对应数据</p>
     *
     * @param freechargeDate 免单时间
     * @return
     * @author lixingxing
     * @date 2017年3月13日 下午5:29:59
     */
    List<YouGetMoneyMeRepayInfo> selectByList(String freechargeDate);
}
