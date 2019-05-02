package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.pay.service.RepaymentService;
import com.vxianjin.gringotts.util.properties.PropertiesConfigUtil;
import com.vxianjin.gringotts.web.dao.IIndexDao;
import com.vxianjin.gringotts.web.pojo.*;
import com.vxianjin.gringotts.web.pojo.index.IndexDto;
import com.vxianjin.gringotts.web.service.IIndexService;
import com.vxianjin.gringotts.web.util.IndexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class IndexService implements IIndexService {

    @Autowired
    private IIndexDao indexDao;
    @Autowired
    private RepaymentService repaymentService;

    /**
     * 查询首页内容
     *
     * @param map
     * @return
     */
    @Override
    public InfoIndex searchInfoIndex(HashMap<String, Object> map) {
        return this.indexDao.searchInfoIndex(map);
    }

    /**
     * 查询公告
     *
     * @param map
     * @return
     */
    @Override
    public List<InfoNotice> searchInfoNoticeByIndex(HashMap<String, Object> map) {
        return this.indexDao.searchInfoNoticeByIndex(map);
    }

    /**
     * 保存首页信息
     *
     * @param indexDto
     */
    @Override
    public void saveIndexDto(IndexDto indexDto) {
        this.indexDao.saveIndexDto(indexDto);
    }

    /**
     * 查询首页信息
     *
     * @param map
     * @return
     */
    @Override
    public IndexDto searchIndexDto(HashMap<String, Object> map) {
        return this.indexDao.searchIndexDto(map);
    }

    /**
     * 动态查询用户信息
     *
     * @param map
     * @return
     */
    @Override
    public InfoIndexInfo searchInfoIndexInfo(HashMap<String, Object> map) {
        return this.indexDao.searchInfoIndexInfo(map);
    }

    /**
     * 获取默认信息
     *
     * @return
     */
    @Override
    public String getDefaultJson() {
        return "{\"code\":\"0\",\"data\":{\"amount_days_list\":{\"amounts\":[\"100000\",\"110000\",\"120000\",\"130000\",\"140000\",\"150000\",\"160000\",\"170000\",\"180000\",\"190000\",\"200000\"],\"creditVet\":[\"8000\",\"16000\"],\"days\":[\"7\",\"14\"],\"accountManage\":[\"8000\",\"12600\"],\"accrual\":[\"1000\",\"1400\"],\"platformUse\":[\"8000\",\"1400\"],\"interests\":[\"0\",\"0\"],\"collectionChannel\":[\"14000\",\"1400\"]},\"item\":{\"card_title\":\"小鱼儿\",\"verify_loan_nums\":\"0\",\"card_amount\":\"200000\",\"card_verify_step\":\"认证0/5\",\"verify_loan_pass\":0,\"new_version_upgrade_url\":\"" + PropertiesConfigUtil.get("APP_HOST_API") + "/getNewAppUpgrade\"},\"user_loan_log_list\":[\"尾号2269，正常还款，成功提额至1050元\",\"尾号6547，成功借款1000元，申请至放款耗时3分钟\",\"尾号2265，成功借款1000元，申请至放款耗时4分钟\",\"尾号3313，正常还款，成功提额至1050元\",\"尾号3369，成功借款1000元，申请至放款耗时3分钟\",\"尾号1225，正常还款，成功提额至1050元\",\"尾号6681，成功借款1000元，申请至放款耗时3分钟\",\"尾号5423，成功借款1000元，申请至放款耗时4分钟\",\"尾号3212，正常还款，成功提额至1050元\",\"尾号7634，成功借款1000元，申请至放款耗时5分钟\",\"尾号8322，成功借款1000元，申请至放款耗时2分钟\",\"尾号8767，正常还款，成功提额至1050元\",\"尾号2729，成功借款1000元，申请至放款耗时3分钟\"],\"today_last_amount\":\"123400\",\"index_images\":[{\"reurl\":\"" + PropertiesConfigUtil.get("APP_HOST_API") + "/gotoAboutIndex\",\"sort\":\"1\",\"title\":\"首页活动三\",\"url\":\"" + PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/index_banner_2.png\"},{\"reurl\":\"" + PropertiesConfigUtil.get("APP_HOST_API") + "/gotoDrawAwardsIndex\",\"sort\":\"2\",\"title\":\"首页活动二\",\"url\":\"" + PropertiesConfigUtil.get("APP_HOST_API") + "/common/web/images/index_banner_1.png\"}]},\"message\":\"访问首页成功\"}";
    }

    /**
     * 获取用户借款信息
     *
     * @return
     */
    @Override
    public String getLoanInfos(InfoIndexInfo indexInfo) {
        HashMap<String, Object> orderMap = new HashMap<String, Object>();
        orderMap.put("USER_ID", indexInfo.getUserId());
        BorrowOrder bo = this.indexDao.searchBorrowOrderByIndex(orderMap);
        return IndexUtil.getLoanInfos(indexInfo, bo, repaymentService);
    }

    /**
     * 根据用户ID存放
     *
     * @param indexInfo
     * @return
     */
    @Override
    public int updateIndexInfoByUserId(InfoIndexInfo indexInfo) {
        return indexDao.updateIndexInfoByUserId(indexInfo);
    }

    /**
     * 处理button按钮事件
     *
     * @param map
     */
    @Override
    public void updateInfoUserInfoBorrowStatus(HashMap<String, Object> map) {
        this.indexDao.updateInfoUserInfoBorrowStatus(map);
    }

    /**
     * 查询user
     *
     * @param map
     * @return
     */
    @Override
    public User searchUserByIndex(HashMap<String, Object> map) {
        return this.indexDao.searchUserByIndex(map);
    }

    /**
     * 查询首页图片
     *
     * @param map
     * @return
     */
    @Override
    public List<InfoImage> searchInfoImage(HashMap<String, Object> map) {
        return this.indexDao.searchInfoImage(map);
    }

    @Override
    public String getIosUpdageImage() {
        return "{\"reurl\": \"http://download.jx-money.com/download.html\",\"sort\": \"2\",\"title\": \"首页活动三\",\"url\": \"http://api.jx-money.com/common/web/images/ios_update.png\"}";
    }

}
