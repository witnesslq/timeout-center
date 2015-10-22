package com.youzan.trade.timeout.dal.dao;

import com.youzan.trade.dataobject.DelayTaskDO;

import java.util.List;

/**
 * @author apple created at: 15/10/22 下午6:05
 */
public interface DelayTaskDAO {

  List<DelayTaskDO> selectAll();
}
