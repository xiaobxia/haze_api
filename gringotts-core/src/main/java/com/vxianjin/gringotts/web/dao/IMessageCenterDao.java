package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.BackMessageCenter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface IMessageCenterDao {

    /**
     * 查询列表
     *
     * @param params receiveUserId收件人主键ID<br>
     *               noticeTypeId消息类型：短信、站内信、邮件<br>
     *               noticeTypeAll查询全部消息类型传入非空值<br>
     *               messageStatus 消息状态：已读未读等<br>
     *               messageAddress 收信地址：邮件地址、手机号码等<br>
     *               messageTitle消息标题<br>
     * @return
     */
    List<BackMessageCenter> findParams(HashMap<String, Object> params);

    /**
     * 根据主键删除
     *
     * @param params ids要删除的主键ID集合<br>
     *               receiveUserId 收信人
     * @return
     */
    int delete(HashMap<String, Object> params);

    /**
     * 根据主键更新
     *
     * @param backMessageCenter
     * @return
     */
    int update(BackMessageCenter backMessageCenter);

    /**
     * 插入
     *
     * @param backMessageCenter
     * @return
     */
    int insert(BackMessageCenter backMessageCenter);

    /**
     * @param params receiveUserId收件人主键ID <br>
     *               noticeTypeId消息类型：短信、站内信、邮件<br>
     *               noticeTypeAll查询全部消息类型传入非空值<br>
     *               messageStatus 消息状态：已读未读等<br>
     *               messageAddress 收信地址：邮件地址、手机号码等<br>
     *               messageTitle消息标题<br>
     * @return
     */
    int findParamsCount(HashMap<String, Object> params);
}
