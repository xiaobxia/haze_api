package com.vxianjin.gringotts.web.pojo;

public class CfcaContractTemplate {

    /**
     * 注意：
     * 	甲方:借款用户
     * 	乙方:出借人 鸾鸟科技
     * 	丙方:法院
     */

    /**
     * 借款用途
     */
    private String useage;
    /**
     * 本金金额人民币
     */
    private String price;
    /**
     * 大写人民币
     */
    private String priceUpper;
    /**
     * 还款金额
     */
    private String repaymentPrice;
    /**
     * 综合服务费
     */
    private String serverPrice;
    /**
     * 年利率
     */
    private String rate;
    /**
     * 还款方式
     */
    private String repaymentMethod;
    /**
     * 期数
     */
    private String repaymentCount;

    /**
     * 乙方ID
     */
    private String userIdTwo;
    /**
     * 乙方姓名
     */
    private String nameTwo;
    /**
     * 乙方证件号
     */
    private String idCardTwo;
    /**
     * 乙方联系地址
     */
    private String addressTwo;
    /**
     * 丙方ID
     */
    private String userIdThree;
    /**
     * 丙方名称
     */
    private String nameThree;
    /**
     * 丙方证件号
     */
    private String idCardThree;
    /**
     * 借款起息日
     */
    private String startTime;
    /**
     * 借款到期日
     */
    private String endTime;
    /**
     * 还款日
     */
    private String repaymentDate;
    /**
     * 创建日期
     */
    private String createTime;
    /**
     * 逾期利率
     */
    private String overdueRate;

    /**
     * 续借利息
     */
    private String againRate;

    public String getUseage() {
        return useage;
    }

    public void setUseage(String useage) {
        this.useage = useage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceUpper() {
        return priceUpper;
    }

    public void setPriceUpper(String priceUpper) {
        this.priceUpper = priceUpper;
    }

    public String getRepaymentPrice() {
        return repaymentPrice;
    }

    public void setRepaymentPrice(String repaymentPrice) {
        this.repaymentPrice = repaymentPrice;
    }

    public String getServerPrice() {
        return serverPrice;
    }

    public void setServerPrice(String serverPrice) {
        this.serverPrice = serverPrice;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getRepaymentCount() {
        return repaymentCount;
    }

    public void setRepaymentCount(String repaymentCount) {
        this.repaymentCount = repaymentCount;
    }

    public String getUserIdTwo() {
        return userIdTwo;
    }

    public void setUserIdTwo(String userIdTwo) {
        this.userIdTwo = userIdTwo;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    public String getIdCardTwo() {
        return idCardTwo;
    }

    public void setIdCardTwo(String idCardTwo) {
        this.idCardTwo = idCardTwo;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public String getUserIdThree() {
        return userIdThree;
    }

    public void setUserIdThree(String userIdThree) {
        this.userIdThree = userIdThree;
    }

    public String getNameThree() {
        return nameThree;
    }

    public void setNameThree(String nameThree) {
        this.nameThree = nameThree;
    }

    public String getIdCardThree() {
        return idCardThree;
    }

    public void setIdCardThree(String idCardThree) {
        this.idCardThree = idCardThree;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getAgainRate() {
        return againRate;
    }

    public void setAgainRate(String againRate) {
        this.againRate = againRate;
    }
}
