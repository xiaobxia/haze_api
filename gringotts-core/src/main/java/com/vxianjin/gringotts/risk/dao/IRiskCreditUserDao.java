package com.vxianjin.gringotts.risk.dao;

import com.vxianjin.gringotts.risk.pojo.RiskCreditUser;
import com.vxianjin.gringotts.risk.pojo.RiskRuleProperty;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface IRiskCreditUserDao {
    /**
     * 更新蚂蚁花呗信息
     *
     * @param riskCreditUser 包含userId、myHb两个信息
     * @return
     */
    int updateUserMyHb(RiskCreditUser riskCreditUser);

    int updateNewFlag(Integer userId);

    /**
     * 更新用户表的芝麻分
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserZmScore(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的关注度信息
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserZmIndusty(RiskCreditUser riskCreditUser);

    /**
     * 根据征信表主键更新征信表芝麻所有信息
     *
     * @param riskCreditUser
     * @return
     */
    int updateZm(RiskCreditUser riskCreditUser);

    /**
     * 运行决策树是更新用户表芝麻的所有信息
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserZm(RiskCreditUser riskCreditUser);

    /**
     * 后台分析时，以用户表信息为准
     *
     * @param id
     * @return 包含个人基本信息、聚信立、芝麻、后台统计数据
     */
    RiskCreditUser findUserDetail(Integer id);

    /**
     * 根据征信表ID更新同盾详情和更新状态为成功
     *
     * @param riskCreditUser
     * @return
     */
    int updateTdDetail(RiskCreditUser riskCreditUser);

    /**
     * 更新白骑士相关信息及接口状态
     *
     * @param riskCreditUser
     * @return
     */
    int updateBqs(RiskCreditUser riskCreditUser);

    /**
     * 更新91征信数据及接口状态
     *
     * @param riskCreditUser
     * @return
     */
    int updateJy(RiskCreditUser riskCreditUser);

    /**
     * 更新密罐数据
     *
     * @param riskCreditUser
     * @return
     */
    int updateMg(RiskCreditUser riskCreditUser);

    /**
     * 更新宜信逾期记录数和更新时间
     *
     * @param riskCreditUser
     * @return
     */
    int updateYx(RiskCreditUser riskCreditUser);

    /**
     * 根据征信表主键更新征信表聚信立所有数据
     *
     * @param riskCreditUser
     * @return
     */
    int updateJxlAndDays(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的聚信立详情
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserJxlDetail(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的聚信立token及状态为已认证
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserJxlToken(RiskCreditUser riskCreditUser);

    /**
     * 更新用户表的聚信立分析数据
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserJxl(RiskCreditUser riskCreditUser);

    List<RiskRuleProperty> findRuleProperty(
            HashMap<String, Object> params);

    /**
     * 查询用户表中聚信立状态是已认证(有token)但没有jxlDetail的记录
     *
     * @return userId 用户主键<br>
     * token 聚信立token<br>
     * firstContactPhone 第一联系人号码<br>
     * secondContactPhone 第二联系人号码
     */
    List<HashMap<String, Object>> findJxlWaitReport();

    /**
     * 定时任务查询待机审的借款申请进行运算
     *
     * @return
     */
    List<HashMap<String, Object>> findWaitAnalysis();

    /**
     * 获得某个用户最近一次被拒绝的时间
     *
     * @param Id
     * @return
     */
    Date findLastFail(Integer Id);

    /**
     * 根据主键查询该待审记录的接口状态
     *
     * @param id
     * @return
     */
    RiskCreditUser findInterfaceStatus(Integer id);

    /**
     * 根据主键更新所有接口状态
     *
     * @param riskCreditUser 包含芝麻、同盾报告ID，同盾状态、白骑士、91、密罐的接口状态
     * @return
     */
    int updateStatus(RiskCreditUser riskCreditUser);

    /**
     * 更新借款申请为机审不通过
     *
     * @param params remark 机审备注<br>
     *               id借款申请主键ID<br>
     * @return
     */
    int updateAssetsFail(HashMap<String, Object> params);

    /**
     * 更新借款申请为机审通过
     *
     * @param params remark 机审备注<br>
     *               id借款申请主键ID<br>
     * @return
     */
    int updateAssetsSuc(HashMap<String, Object> params);

    /**
     * 更新征信待审核表的状态和备注
     *
     * @param riskCreditUser
     * @return
     */
    int updateRiskStatus(RiskCreditUser riskCreditUser);

    /**
     * 根据主键查询征信详情
     *
     * @return
     */
    RiskCreditUser findById(Integer id);

    /**
     * 查询当前地址出现的次数(包括被查用户的那一次)
     *
     * @param presentAddress 用户当前住址
     * @return
     */
    Integer findRepeatZz(String presentAddress);

    /**
     * 查询公司地址出现的次数(包括被查用户的那一次)
     *
     * @param presentAddress 用户公司地址
     * @return
     */
    Integer findRepeatGsdz(String companyAddress);

    /**
     * 查询公司名称出现的次数(包括被查用户的那一次)
     *
     * @param companyName 用户公司名称
     * @return
     */
    Integer findRepeatGsmc(String companyName);

    /**
     * 查询使用同一个设备注册的用户数量，不能排除当前用户
     *
     * @param equipmentNumber 用户注册设备号
     * @return
     */
    Integer findDifPhoneByEqNum(String equipmentNumber);

    /**
     * 1.查询公司电话相同但公司名称或者公司地址不同的记录数，减1是排除当前用户
     * 2.查询公司地址相同但公司名称或者公司电话不同的记录数，减1是排除当前用户
     * 3.查询公司名称相同但公司号码或者公司地址不同的记录数，减1是排除当前用户 <br>
     * 4.查询使用同一个用户使用不同设备登录的用户数量，不能排除当前用户 <br>
     * 5.查询某个用户的第一二联系人在平台的申请记录
     *
     * @param riskCreditUser
     * @return
     */
    List<Integer> findAllCount(RiskCreditUser riskCreditUser);

    /**
     * 更新聚类信息
     *
     * @param riskCreditUser
     * @return
     */
    int updateJl(RiskCreditUser riskCreditUser);

    /**
     * 查询出实名认证通过、聚信立认证通过并且返回报告信息的、芝麻认证通过的，状态是未计算的用户列表，用于计算用户最新额度
     *
     * @return
     */
    List<RiskCreditUser> findCalMoney();

    /**
     * 根据用户主键把用户表的额度、剩余可用额度更新为机身额度、时间更新为当前时间，是否计算过借款额度更新为已更新
     *
     * @return
     */
    int updateUserMoney(RiskCreditUser riskCreditUser);

    /**
     * 插入一条征信记录，辅助用户额度的计算
     */
    int insertCalMoney(RiskCreditUser riskCreditUser);

    /**
     * 更新征信表的机审额度
     *
     * @param riskCreditUser
     * @return
     */
    int updateMoney(RiskCreditUser riskCreditUser);

    /**
     * 主键ID
     *
     * @param id
     * @return
     */
    RiskCreditUser findBorrowMoney(Integer id);

    /**
     * 更新用户表的可用金额
     *
     * @param riskCreditUser 用户主键、可用金额（在sql中乘以100）
     * @return
     */
    int updateUserAvailable(RiskCreditUser riskCreditUser);

    /**
     * 根据用户ID查询出现黑名单、逾期情况的记录数
     *
     * @param userId
     * @return
     */
    List<RiskCreditUser> findByUserId(Integer userId);

    /**
     * 更新同盾信息
     *
     * @param riskCreditUser
     * @return
     */
    int updateUserTd(RiskCreditUser riskCreditUser);

    int updateTdNewFlag(Integer userId);

    /**
     * 查询某个用户是否有计算额度的记录
     *
     * @param riskCreditUser userId和assetId（固定传入0）
     * @return
     */
    Integer findOneCal(RiskCreditUser riskCreditUser);
}
