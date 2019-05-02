package com.vxianjin.gringotts.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述:
 * 贷款超市Controller
 *
 * @author zed
 * @since 2019-02-15 3:14 PM
 */
@Controller
public class ShopController {
    @RequestMapping("getShopList")
    public String getShop(){
        return "shop/shop";
    }

}

