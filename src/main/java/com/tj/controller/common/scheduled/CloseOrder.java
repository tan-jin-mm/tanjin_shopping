package com.tj.controller.common.scheduled;

import com.tj.service.IOrderService;
import com.tj.utils.PropertiesUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CloseOrder {
    @Autowired
    private IOrderService iOrderService;
    @Scheduled(cron="* * 0/1 * * *")
    private void closeorder(){
        String date = com.tj.utils.DateUtils.dateToStr(DateUtils.addHours(new Date(), -PropertiesUtils.HOUR));
        iOrderService.closeOrderByScheduled(date);
        System.out.println("====订单关闭=====");
    }




}
