package com.vxianjin.gringotts.web.controller;

import com.vxianjin.gringotts.web.pojo.HeartAlive;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chenkai
 * @Date: 2018/6/29 14:11
 * @Description:
 */
@RestController
@RequestMapping("/heart")
public class HeartBeatController {

    @RequestMapping("/alive")
    public String heartAliveLine(){
        return new HeartAlive().toString();
    }
}
