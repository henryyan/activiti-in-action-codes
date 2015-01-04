package me.kafeitu.activiti.web.chapter14;

import me.kafeitu.activiti.chapter13.Page;
import me.kafeitu.activiti.chapter13.PageUtil;
import org.activiti.engine.ManagementService;
import org.activiti.engine.management.TableMetaData;
import org.activiti.engine.management.TablePage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 数据库查询控制器
 * User: henryyan
 */
@Controller
@RequestMapping("/chapter14/database")
public class DatabaseController {

    @Autowired
    ManagementService managementService;

    @RequestMapping("")
    public ModelAndView index(@RequestParam(value = "tableName", required = false) String tableName, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("chapter14/database");

        // 读取表
        Map<String, Long> tableCount = managementService.getTableCount();
        mav.addObject("tableCount", tableCount);

        // 读取表记录
        if (StringUtils.isNotBlank(tableName)) {
            TableMetaData tableMetaData = managementService.getTableMetaData(tableName);
            mav.addObject("tableMetaData", tableMetaData);
            Page<Map<String, Object>> page = new Page<Map<String, Object>>(10);
            int[] pageParams = PageUtil.init(page, request);
            TablePage tablePages = managementService.createTablePageQuery().tableName(tableName).listPage(pageParams[0], pageParams[1]);

            page.setResult(tablePages.getRows());
            page.setTotalCount(tableCount.get(tableName));
            mav.addObject("page", page);
        }
        return mav;
    }

}
