package com.vxianjin.gringotts.risk.utils.yx;

import java.util.List;

public class YxSendData {
    private List<YxLoanRecords> loanRecords;
    private List<YxRiskResults> riskResults;

    public List<YxLoanRecords> getLoanRecords() {
        return loanRecords;
    }

    public void setLoanRecords(List<YxLoanRecords> loanRecords) {
        this.loanRecords = loanRecords;
    }

    public List<YxRiskResults> getRiskResults() {
        return riskResults;
    }

    public void setRiskResults(List<YxRiskResults> riskResults) {
        this.riskResults = riskResults;
    }

}
