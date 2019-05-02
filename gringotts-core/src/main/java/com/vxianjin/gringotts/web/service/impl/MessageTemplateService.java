package com.vxianjin.gringotts.web.service.impl;

import com.vxianjin.gringotts.web.dao.IMessageTemplateDao;
import com.vxianjin.gringotts.web.pojo.MessageTemplate;
import com.vxianjin.gringotts.web.service.IMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageTemplateService implements IMessageTemplateService {

    @Autowired
    private IMessageTemplateDao messageTemplateDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {

        return messageTemplateDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(MessageTemplate record) {
        // TODO Auto-generated method stub
        return messageTemplateDao.insert(record);
    }

    @Override
    public int insertSelective(MessageTemplate record) {
        // TODO Auto-generated method stub
        return messageTemplateDao.insertSelective(record);
    }

    @Override
    public MessageTemplate selectByPrimaryKey(Integer id) {
        // TODO Auto-generated method stub
        return messageTemplateDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(MessageTemplate record) {
        // TODO Auto-generated method stub
        return messageTemplateDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MessageTemplate record) {
        // TODO Auto-generated method stub
        return messageTemplateDao.updateByPrimaryKey(record);
    }

    @Override
    public MessageTemplate findTemplate(MessageTemplate record) {
        return messageTemplateDao.findTemplate(record);
    }


}