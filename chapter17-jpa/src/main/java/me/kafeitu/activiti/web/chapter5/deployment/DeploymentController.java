package me.kafeitu.activiti.web.chapter5.deployment;

import me.kafeitu.activiti.chapter13.Page;
import me.kafeitu.activiti.chapter13.PageUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * 部署流程
 *
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter5")
public class DeploymentController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    IdentityService identityService;

    @Autowired
    ManagementService managementService;

    /**
     * 流程定义列表
     */
    @RequestMapping(value = "/process-list")
    public ModelAndView processList(HttpServletRequest request) {

        // 对应WEB-INF/views/chapter5/process-list.jsp
        String viewName = "chapter5/process-list";

        Page<ProcessDefinition> page = new Page<ProcessDefinition>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);

        ModelAndView mav = new ModelAndView(viewName);
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);

        page.setResult(processDefinitionList);
        page.setTotalCount(processDefinitionQuery.count());
        mav.addObject("page", page);

        // 读取所有人员
        List<User> users = identityService.createUserQuery().list();
        mav.addObject("users", users);

        // 读取所有组
        List<Group> groups = identityService.createGroupQuery().list();
        mav.addObject("groups", groups);

        // 读取每个流程定义的候选属性
        Map<String, Map<String, List<? extends Object>>> linksMap = setCandidateUserAndGroups(processDefinitionList);
        mav.addObject("linksMap", linksMap);

        return mav;
    }

    /**
     * 读取流程定义的相关候选启动人、组，根据link信息转换并封装为User、Group对象
     * @param processDefinitionList
     * @return
     */
    private Map<String, Map<String, List<? extends Object>>> setCandidateUserAndGroups(List<ProcessDefinition> processDefinitionList) {
        Map<String, Map<String, List<? extends Object>>> linksMap = new HashMap<String, Map<String, List<? extends Object>>>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());

            Map<String, List<? extends Object>> single = new Hashtable<String, List<? extends Object>>();
            List<User> linkUsers = new ArrayList<User>();
            List<Group> linkGroups = new ArrayList<Group>();

            for (IdentityLink link : identityLinks) {
                if (StringUtils.isNotBlank(link.getUserId())) {
                    linkUsers.add(identityService.createUserQuery().userId(link.getUserId()).singleResult());
                } else if (StringUtils.isNotBlank(link.getGroupId())) {
                    linkGroups.add(identityService.createGroupQuery().groupId(link.getGroupId()).singleResult());
                }
            }

            single.put("user", linkUsers);
            single.put("group", linkGroups);

            linksMap.put(processDefinition.getId(), single);

        }
        return linksMap;
    }

    /**
     * 流程定义列表--过滤激活的流程定义
     */
    @RequestMapping(value = "/process-list-view")
    public ModelAndView processListReadonly(HttpServletRequest request) {

        // 对应WEB-INF/views/chapter5/process-list.jsp
        String viewName = "chapter5/process-list-view";

        Page<ProcessDefinition> page = new Page<ProcessDefinition>(PageUtil.PAGE_SIZE);
        int[] pageParams = PageUtil.init(page, request);

        ModelAndView mav = new ModelAndView(viewName);
//        User user = UserUtil.getUserFromSession(request.getSession());
//        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().startableByUser(user.getId());
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        processDefinitionQuery.suspended().active();

        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);

        page.setResult(processDefinitionList);
        page.setTotalCount(processDefinitionQuery.count());
        mav.addObject("page", page);

        // 读取每个流程定义的候选属性
        Map<String, Map<String, List<? extends Object>>> linksMap = setCandidateUserAndGroups(processDefinitionList);
        mav.addObject("linksMap", linksMap);

        return mav;
    }

    /**
     * 部署流程资源
     */
    @RequestMapping(value = "/deploy")
    public String deploy(@RequestParam(value = "file", required = true) MultipartFile file) {

        // 获取上传的文件名
        String fileName = file.getOriginalFilename();

        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = file.getInputStream();

            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);

            // zip或者bar类型的文件用ZipInputStream方式部署
            DeploymentBuilder deployment = repositoryService.createDeployment();
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment.addZipInputStream(zip);
            } else {
                // 其他类型的文件直接部署
                deployment.addInputStream(fileName, fileInputStream);
            }
            deployment.deploy();
        } catch (Exception e) {
            logger.error("error on deploy process, because of file input stream");
        }

        return "redirect:process-list";
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @RequestMapping(value = "/read-resource")
    public void readResource(@RequestParam("pdid") String processDefinitionId,
                             @RequestParam("resourceName") String resourceName, HttpServletResponse response)
            throws Exception {
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 删除部署的流程，级联删除流程实例
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/delete-deployment")
    public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:process-list";
    }

}
