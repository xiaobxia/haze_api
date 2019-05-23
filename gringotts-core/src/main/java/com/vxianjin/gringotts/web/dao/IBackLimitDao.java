package com.vxianjin.gringotts.web.dao;

import com.vxianjin.gringotts.pay.model.BackLimit;
import org.springframework.stereotype.Repository;

/**
 * @author fully 2019-05-23
 */
@Repository
public interface IBackLimitDao {

    BackLimit selectById(int id);

}
