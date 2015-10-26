package com.youzan.trade.timeout.executor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author apple created at: 15/10/23 下午9:20
 */
@Component("taskJob")
public class TaskJob {

  @Scheduled(cron = "0/2 * * * * ?")
  public void job1() {
    System.out.println("running");
  }

}
