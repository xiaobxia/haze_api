package com.vxianjin.gringotts.web.service;

import com.vxianjin.gringotts.common.ResponseContent;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * @author Administrator
 */
@Service
public interface IPushUserService {

    ResponseContent addPushUserApproves(HashMap<String, Object> params);

}
