package me.kafeitu.activiti.web.chapter6;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import me.kafeitu.activiti.web.base.AbstractController;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 流程定义相关功能：读取动态表单字段、读取外置表单内容
 *
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter6")
public class ProcessDefinitionController extends AbstractController {

    /**
     * 读取启动流程的表单字段
     */
    @RequestMapping(value = "getform/start/{processDefinitionId}")
    public ModelAndView readStartForm(@PathVariable("processDefinitionId") String processDefinitionId) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        // 根据是否有formkey属性判断使用哪个展示层
        String viewName = "chapter6/start-process-form";
        ModelAndView mav = new ModelAndView(viewName);

        // 判断是否有formkey属性
        if (hasStartFormKey) {
            Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
            mav.addObject("startFormData", renderedStartForm);
            mav.addObject("processDefinition", processDefinition);
        } else { // 动态表单字段
            StartFormData startFormData = formService.getStartFormData(processDefinitionId);
            mav.addObject("startFormData", startFormData);
        }
        mav.addObject("hasStartFormKey", hasStartFormKey);
        mav.addObject("processDefinitionId", processDefinitionId);
        return mav;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "process-instance/start/{processDefinitionId}")
    public String startProcessInstance(@PathVariable("processDefinitionId") String pdid, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(pdid).singleResult();
        boolean hasStartFormKey = processDefinition.hasStartFormKey();

        Map<String, String> formValues = new HashMap<String, String>();

        if (hasStartFormKey) { // formkey表单
            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
            for (Entry<String, String[]> entry : entrySet) {
                String key = entry.getKey();
                formValues.put(key, entry.getValue()[0]);
            }
        } else { // 动态表单
            // 先读取表单字段在根据表单字段的ID读取请求参数值
            StartFormData formData = formService.getStartFormData(pdid);

            // 从请求中获取表单字段的值
            List<FormProperty> formProperties = formData.getFormProperties();
            for (FormProperty formProperty : formProperties) {
                String value = request.getParameter(formProperty.getId());
                formValues.put(formProperty.getId(), value);
            }
        }

        // 获取当前登录的用户
        User user = UserUtil.getUserFromSession(request.getSession());
        // 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
        if (user == null || StringUtils.isBlank(user.getId())) {
            return "redirect:/login?timeout=true";
        }
        identityService.setAuthenticatedUserId(user.getId());

        // 提交表单字段并启动一个新的流程实例
        ProcessInstance processInstance = formService.submitStartFormData(pdid, formValues);
        logger.debug("start a processinstance: {}", processInstance);
        redirectAttributes.addFlashAttribute("message", "流程已启动，实例ID：" + processInstance.getId());
        return "redirect:/chapter5/process-list";
    }

}
