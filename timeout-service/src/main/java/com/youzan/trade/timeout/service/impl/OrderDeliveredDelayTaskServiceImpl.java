package com.youzan.trade.timeout.service.impl;

import com.youzan.api.common.response.CommonResultCode;
import com.youzan.api.common.response.PlainResult;
import com.youzan.trade.timeout.api.order.delivered.OrderDeliveredDelayTaskService;
import com.youzan.trade.timeout.api.order.delivered.model.DelayParams;
import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.util.LogUtils;

import com.alibaba.dubbo.config.annotation.Service;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import lombok.extern.slf4j.Slf4j;

/**
 * 这里的path, 尽量按restful的风格
 *
 * @author apple created at: 16/1/14 上午11:39
 */
@Path("order/delivered")
@Service(protocol = {"dubbo", "rest"})
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
  public PlainResult<Integer> increaseDelayEndTime(DelayParams delayParams) {
    LogUtils.info(log, "订单已发货的延时任务, 延长任务到期时间, 参数: {}", delayParams);

    PlainResult<Integer> baseResult = new PlainResult<>();
    baseResult.setCode("0");//reset
    String orderNo = delayParams.getBizId();

    try {
      Integer result =
          delayTaskService
              .increaseDelayEndTimeByBizTypeAndBizId(bizType, orderNo, defaultIncrementInDays);
      if (result != null && result > 0) {
        // 更新数据库不成功,且不抛异常,暂时按异常处理
        LogUtils.info(log, "[SUCC]订单已发货的延时任务, 延长任务到期时间, 执行成功");
        baseResult.setData(result);
      } else {
        baseResult.setCode("0");
      }
    } catch (Exception e) {
      LogUtils.error(log, "[FAIL]订单已发货的延时任务, 延长任务到期时间, 发生异常", e);
      baseResult.setError(CommonResultCode.EXCEPTION, e.getMessage());
    }

    return baseResult;
  }

  @POST
  @Path("delay/finish")
  @Override
  public PlainResult<Boolean> finishDelayTask(DelayParams delayParams) {
    LogUtils.info(log, "订单已发货的延时任务, 延长任务到期时间, 参数: {}", delayParams);

    PlainResult<Boolean> plainResult = new PlainResult<>();
    plainResult.setCode("0");//reset

    String orderNo = delayParams.getBizId();

    if (StringUtils.isBlank(orderNo)) {
      LogUtils.error(log, "[FinishDelayTask]BizId should not be null.");
      plainResult.setError(CommonResultCode.ILLEGAL_PARAM);
      return plainResult;
    }

    try {
      DelayTask
          delayTask =
          delayTaskService.getTaskByBizTypeAndBizId(BizType.DELIVERED_ORDER.code(), orderNo);
      if (delayTask == null) {
        LogUtils.info(log, "[FinishDelayTask]Task not found.bizId={},bizType={}", orderNo,
                      BizType.DELIVERED_ORDER.code());
        return plainResult;
      }
      Boolean result =
          delayTaskService.closeOnSuccess(delayTask.getId(), CloseReason.MANUAL_SUCCESS);
      plainResult.setData(result);
    } catch (Exception e) {
      LogUtils.error(log, "[FAIL]订单已发货的延时任务, 延长任务到期时间, 发生异常", e);
      plainResult.setError(CommonResultCode.EXCEPTION, e.getMessage());
    }

    return plainResult;
  }

}
