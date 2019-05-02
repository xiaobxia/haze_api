package com.vxianjin.gringotts.pay.enums;

/**
 * @Author: chenkai
 * @Date: 2018/8/21 14:23
 * @Description:
 */
public enum OrderChangeAction {
    RENEWAL_ACTION("RENEWAL_ACTION", "续期操作"),
    REPAYL_ACTION("REPAYL_ACTION", "还款操作"),
    PART_REPAYL_ACTION("PART_REPAYL_ACTION","部分还款操作"),
    UNLINE_TRANSFER_ACTION("UNLINE_TRANSFER_ACTION", "线下转账"),
    BORROW_ACTION("BORROW_ACTION", "借款操作"),
    COLLECTION_ACTION("COLLECTION_ACTION", "催收扣款"),
    COLLECTION_JIANMIAN_ACTION("COLLECTION_JIANMIAN_ACTION", "催收减免扣款"),
    COLLECTION_PART_JIANMIAN_ACTION("COLLECTION_PART_JIANMIAN_ACTION", "催收部分减免扣款"),
    UNLINE_RENEWAL_ACTION("UNLINE_RENEWAL_ACTION", "线下续期操作"),
    UNLINE_REPAYL_ACTION("UNLINE_REPAYL_ACTION", "线下还款操作"),
    UNLINE_PART_REPAYL_ACTION("UNLINE_PART_REPAYL_ACTION", "线下部分还款操作"),
    MACHINE_AUDITING("MACHINE_AUDITING", "机审订单"),
    MAN_AUDITING("MAN_AUDITING", "人工复审"),
    SKIP_MACHINE("SKIP_MACHINE", "跳过机审"),
    OVERDUE("OVERDUE", "逾期"),
    FANGK_AUDITING("FANGK_AUDITING", "放款审核");


    OrderChangeAction(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderChangeAction getByCode(String code){
        return OrderChangeAction.valueOf(code);
    }
}
