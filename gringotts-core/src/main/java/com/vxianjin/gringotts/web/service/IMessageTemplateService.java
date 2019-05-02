package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.web.pojo.MessageTemplate;

public interface IMessageTemplateService {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageTemplate record);

    int insertSelective(MessageTemplate record);

    MessageTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageTemplate record);

    int updateByPrimaryKey(MessageTemplate record);

    MessageTemplate findTemplate(MessageTemplate record);

}