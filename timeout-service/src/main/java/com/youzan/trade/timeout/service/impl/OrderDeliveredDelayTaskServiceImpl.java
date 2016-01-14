package com.youzan.trade.timeout.service.impl;

import com.youzan.api.common.response.BaseResult;
import com.youzan.api.common.response.CommonResultCode;
import com.youzan.trade.timeout.api.order.delivered.OrderDeliveredDelayTaskService;
import com.youzan.trade.timeout.api.order.delivered.model.DelayParams;
import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.util.LogUtils;

import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import lombok.extern.slf4j.Slf4j;

/**
 * 这里的path, 尽量按restful的风格
 *
 * @author apple created at: 16/1/14 上午11:39
 */
@Path("timeout/order/delivered")
@Service(protocol = { "dubbo", "rest" })
@org.springframework.stereotype.Service
@Slf4j
public class OrderDeliveredDelayTaskServiceImpl implements OrderDeliveredDelayTaskService {

  @Resource
  private DelayTaskService delayTaskService;

  private int defaultIncrementInDays = 2;

  private int bizType = BizType.DELIVERED_ORDER.code();

  @POST
  @Path("delay/increase")
  @Override
  public BaseResult increaseDelayEndTime(DelayParams delayParams) {
    LogUtils.info(log, "订单已发货的延时任务, 延长任务到期时间, 参数: {}", delayParams);

    BaseResult baseResult = new BaseResult();

    String orderNo = delayParams.getBizId();

    try {
      boolean result =
          delayTaskService.increaseDelayEndTimeByBizTypeAndBizId(bizType, orderNo, defaultIncrementInDays);
      if (!result) {
        // 更新数据库不成功,且不抛异常,暂时按异常处理
        LogUtils.error(log, "订单已发货的延时任务, 延长任务到期时间, 执行失败");
        baseResult.setError(CommonResultCode.EXCEPTION);
      }
    } catch (Exception e) {
      LogUtils.error(log, "订单已发货的延时任务, 延长任务到期时间, 发生异常", e);
      baseResult.setError(CommonResultCode.EXCEPTION);
    }

    return baseResult;
  }

}
