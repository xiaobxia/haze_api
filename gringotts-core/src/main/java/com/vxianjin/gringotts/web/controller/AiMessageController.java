package com.vxianjin.gringotts.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.pay.component.OrderLogComponent;
import com.vxianjin.gringotts.pay.enums.OperateType;
import com.vxianjin.gringotts.pay.pojo.OrderLogModel;
import com.vxianjin.gringotts.web.pojo.BorrowOrder;
import com.vxianjin.gringotts.web.pojo.User;
import com.vxianjin.gringotts.web.service.IBorrowOrderService;
import com.vxianjin.gringotts.web.service.IUserService;
import com.vxianjin.gringotts.web.util.aliyun.RocketMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * ai message
 *
 * @author zed
 * @since 2019-02-12 3:08 PM
 */
@RestController
@RequestMapping("robot")
public class AiMessageController {
    private static final Logger log = LoggerFactory.getLogger(AiMessageController.class);
    @Resource
    private OrderLogComponent orderLogComponent;
    @Resource
    private IUserService userService;
    @Resource
    private IBorrowOrderService borrowOrderService ;
    @PostMapping("mkCallBack")
    public String mkCallBack(@RequestBody String data){
        log.info("mkCallBackData:{}",data);
        JSONArray jsonArray = JSON.parseArray(data);
        if(jsonArray!=null &&jsonArray.size()>0){
            for(Object o :jsonArray){
                JSONObject object = (JSONObject) o;
                if(object!=null){
                    String phone = object.getString("Tel");
                    int holdLen = object.getIntValue("HoldLen");
                    int result = object.getIntValue("Result");
//                String level = object.getString("IntentLevel");
                    Integer loanStatus;
                    /*
                     * 接通状态 并且通话时长 > 5 s
                     */
                    if(result==0 && holdLen > 5){
                        loanStatus= BorrowOrder.STATUS_FKZ;
                    }else{
                        loanStatus= BorrowOrder.STATUS_AI_FAIL;
                    }
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("userPhone",phone);
                    User user=userService.searchUserByCheckTel(map);
                    if(user!=null){
                        BorrowOrder borrowOrder =borrowOrderService.selectBorrowOrderNowUseId(Integer.valueOf(user.getId()));
                        if(borrowOrder!=null){
                            /*
                             * AI 待验证或 AI验证失败状态 更新订单状态
                             */
                            if(BorrowOrder.STATUS_AI.equals(borrowOrder.getStatus())|| BorrowOrder.STATUS_AI_FAIL.equals(borrowOrder.getStatus())){
                                borrowOrderService.updateBorrowStatus(String.valueOf(borrowOrder.getId()),String.valueOf(loanStatus));
                                //订单修改日志记录
                                OrderLogModel logModel = new OrderLogModel();
                                logModel.setUserId(String.valueOf(user.getId()));
                                logModel.setBorrowId(String.valueOf(borrowOrder.getId()));
                                logModel.setOperateType(OperateType.BORROW.getCode());
                                logModel.setBeforeStatus(String.valueOf(borrowOrder.getStatus()));
                                logModel.setAfterStatus(String.valueOf(loanStatus));
                                orderLogComponent.addNewOrderLog(logModel);
                            }
                        }
                    }
                }
            }

        }

        Map<String,Object> map = new HashMap<>();
        map.put("code",200);
        map.put("msg","OK");
        return JSON.toJSONString(map);
    }
    @PostMapping("callBack")
    public String robotCallBack(@RequestBody String dataStr){
        log.info("dataStr:{}",dataStr);
        JSONObject jsonObject = JSON.parseObject(dataStr);
        if(jsonObject!=null){
            JSONObject dataObject =jsonObject.getJSONObject("data");
            if(dataObject!=null){
                JSONObject data = dataObject.getJSONObject("data");
                if(data!=null){
                    JSONObject sceneInstance = data.getJSONObject("sceneInstance");
                    if(sceneInstance!=null){
                        int duration = sceneInstance.getIntValue("duration");
                        int finishStatus = sceneInstance.getIntValue("finishStatus");
                        Integer loanStatus;
                        /*
                         * 接通状态 并且通话时长 > 5 s
                         */
                        if(finishStatus==0 && duration > 5){
                            loanStatus= BorrowOrder.STATUS_FKZ;
                        }else{
                            loanStatus= BorrowOrder.STATUS_AI_FAIL;
                        }
                        String userPhone = sceneInstance.getString("customerTelephone");
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("userPhone",userPhone);
                        User user=userService.searchUserByCheckTel(map);
                        if(user!=null){
                            BorrowOrder borrowOrder =borrowOrderService.selectBorrowOrderNowUseId(Integer.valueOf(user.getId()));
                            if(borrowOrder!=null){
                                /*
                                 * AI 待验证或 AI验证失败状态 更新订单状态
                                 */
                                if(BorrowOrder.STATUS_AI.equals(borrowOrder.getStatus())|| BorrowOrder.STATUS_AI_FAIL.equals(borrowOrder.getStatus())){
                                    borrowOrderService.updateBorrowStatus(String.valueOf(borrowOrder.getId()),String.valueOf(loanStatus));
                                    //订单修改日志记录
                                    OrderLogModel logModel = new OrderLogModel();
                                    logModel.setUserId(String.valueOf(user.getId()));
                                    logModel.setBorrowId(String.valueOf(borrowOrder.getId()));
                                    logModel.setOperateType(OperateType.BORROW.getCode());
                                    logModel.setBeforeStatus(String.valueOf(borrowOrder.getStatus()));
                                    logModel.setAfterStatus(String.valueOf(loanStatus));
                                    orderLogComponent.addNewOrderLog(logModel);
                                }
                            }
                        }
                    }
                }
            }
        }
        return "success";
    }

    /**
     * 用于手动触发待AI验证
     * @return res
     */
    @RequestMapping("executeAiCall")
    public String callBy(){
        List<BorrowOrder> list = borrowOrderService.getWaitAiList();
        if(list!=null && !list.isEmpty()){
            log.info("待ai 验证数量:{}",list.size());
            for(BorrowOrder order :list){
                User user = userService.searchByUserid(order.getUserId());
                Map<String,String> map = new HashMap<>();
                map.put("phone",user.getUserPhone());
                map.put("name",user.getRealname());
                RocketMqUtil.sendAiMessage(JSON.toJSONString(map));
            }
        }
        return "success";
    }
}

