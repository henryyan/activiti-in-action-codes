package me.kafeitu.activiti.web.chapter21;

import me.kafeitu.activiti.chapter13.Page;
import me.kafeitu.activiti.chapter13.PageUtil;
import org.activiti.engine.ManagementService;
import org.activiti.engine.event.EventLogEntry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局事件日志
 * @author: Henry Yan
 */
@Controller
@RequestMapping("/chapter21/event/log")
public class EventLogController {

    @Autowired
    ManagementService managementService;

    @Autowired
    SessionFactory sessionFactory;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String eventLogList(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<Long, String> datas = new HashMap<Long, String>();
        Page<EventLogEntry> page = new Page<EventLogEntry>(PageUtil.PAGE_SIZE);
        long[] pageParams = PageUtil.initForLong(page, request);
        List<EventLogEntry> logEntries = managementService.getEventLogEntries(pageParams[0], pageParams[1]);
        page.setResult(logEntries);
        for (EventLogEntry logEntry : logEntries) {
            datas.put(logEntry.getLogNumber(), new String(logEntry.getData(), "UTF-8"));
        }

        Session session = sessionFactory.openSession();

        BigInteger o = (BigInteger) session.createSQLQuery("select count(*) from ACT_EVT_LOG").uniqueResult();
        page.setTotalCount(o.longValue());
        session.close();

        request.setAttribute("page", page);
        request.setAttribute("datas", datas);

        return "chapter21/event-log";
    }

}
