package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.pay.model.BackExtend;
import org.springframework.stereotype.Repository;

/**
 * @author fully 2019-05-23
 */
@Repository
public interface IBackExtendDao {

    BackExtend selectById(int id);

    Integer selectStatusByExtendId(int id);

    BackExtend selectByProductId(int prodcutId);

    BackExtend selectByBorrowOrderId(int BorrowOrderId);

}
