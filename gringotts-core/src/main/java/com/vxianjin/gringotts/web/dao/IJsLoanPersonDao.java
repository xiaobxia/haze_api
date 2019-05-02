package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.web.pojo.JsLoanPerson;

import java.util.List;

public interface IJsLoanPersonDao {

    List<JsLoanPerson> findUserList(JsLoanPerson jsLoanPerson);


    void insert(JsLoanPerson jsLoanPerson);


    void update(JsLoanPerson jsLoanPerson);

}
