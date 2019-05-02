package com.vxianjin.gringotts.web.util.autorisk;

import com.vxianjin.gringotts.risk.pojo.*;
import com.vxianjin.gringotts.web.dao.IBackConfigParamsDao;
import com.vxianjin.gringotts.web.pojo.BackConfigParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AutoRiskUntil {

    /**
     * 评估器汇总建议
     *
     * @param reasons
     * @return
     */
    public static Advice evaluatorStrategy(List<Advice> reasons) {
        if (reasons != null && reasons.contains(Advice.REJECT)) {
            return Advice.REJECT;
        }
        if (reasons != null && reasons.contains(Advice.REVIEW)) {
            return Advice.REVIEW;
        }
        return Advice.PASS;
    }

    /**
     * 是否走人工
     * @param riskModel
     * @return
     */
    public static boolean isArtificial(RiskModel riskModel){
        return isArtificial(riskModel.getModelCode());
    }

    /**
     * 是否走人工
     * @param riskModelSwitch
     * @return
     */
    public static boolean isArtificial(RiskModelSwitch riskModelSwitch){
        return isArtificial(riskModelSwitch.getModelCode());
    }

    /**
     * 是否走人工
     * @param modelCode
     * @return
     */
    public static boolean isArtificial(String modelCode){
        return "0".equals(modelCode);
    }

    /**
     * 获取人工审核的模型参数
     *
     * @return
     */
    public static RiskModel getArtificialRiskModel() {
        RiskModel res = new RiskModel();

        res.setModelCode("0");
        res.setVariableVersion(0);
        res.setBinningVersion(0);
        res.setCutoffVersion(0);
        res.setCanIgnoreVersion(0);

        res.setCutoff(-1);
        res.setReviewDown(-1);
        res.setReviewUp(-1);

        return res;
    }

}
