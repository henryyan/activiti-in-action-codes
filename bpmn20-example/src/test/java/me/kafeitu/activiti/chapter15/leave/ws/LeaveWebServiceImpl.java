package me.kafeitu.activiti.chapter15.leave.ws;

import jodd.datetime.JDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 请假WebService接口简单实现
 * @author: Henry Yan
 */
@WebService(endpointInterface = "me.kafeitu.activiti.chapter15.leave.ws.LeaveWebService", serviceName = "LeaveWebService")
public class LeaveWebServiceImpl implements LeaveWebService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 计算开始日期与结束日期相差天数
     * @throws ParseException
     */
    public int daysBetween(Date startDate, Date endDate) {
        JDateTime joddStartDate = new JDateTime(startDate);
        JDateTime joddEndDate = new JDateTime(endDate);
        int result = joddStartDate.daysBetween(joddEndDate);
        logger.info("日期比较：startDate={}, endDate={}, 相差 {} 天。", startDate, endDate, result);
        return result;
    }

    @Override
    public boolean generalManagerAudit(String startDate, String endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int days = 0;
        try {
            days = daysBetween(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
        } catch (ParseException e) {
            logger.error("解析日期出错：startDate={}, endDate={}", startDate, endDate);
        }
        return days > 3 ? true : false;
    }
}