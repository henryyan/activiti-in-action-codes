package me.kafeitu.activiti.web.chapter12;

import me.kafeitu.activiti.chapter6.util.UserUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 附件控制器
 *
 * @author henryyan
 */
@Controller
@RequestMapping(value = "/chapter12/attachment")
public class AttachmentController {

    @Autowired
    TaskService taskService;

    @Autowired
    IdentityService identityService;

    /**
     * 文件类型的附件
     */
    @RequestMapping(value = "new/file")
    public String newFile(@RequestParam("taskId") String taskId, @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                          @RequestParam("attachmentName") String attachmentName, @RequestParam(value = "attachmentDescription", required = false) String attachmentDescription,
                          @RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            String attachmentType = file.getContentType() + ";" + FilenameUtils.getExtension(file.getOriginalFilename());
            identityService.setAuthenticatedUserId(UserUtil.getUserFromSession(session).getId());
            Attachment attachment = taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription,
                    file.getInputStream());
            taskService.saveAttachment(attachment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/chapter6/task/getform/" + taskId;
    }

    /**
     * URL类型的附件
     */
    @RequestMapping(value = "new/url")
    public String newUrl(@RequestParam("taskId") String taskId, @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
                         @RequestParam("attachmentName") String attachmentName, @RequestParam(value = "attachmentDescription", required = false) String attachmentDescription,
                         @RequestParam("url") String url, HttpSession session) {
        String attachmentType = "url";
        identityService.setAuthenticatedUserId(UserUtil.getUserFromSession(session).getId());
    /*Attachment attachment = */
        taskService.createAttachment(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, url);
    /*
     * 如果要更新附件内容，先读取附件对象，然后设置属性（只能更新name和description），最后保存附件对象
     */
//    taskService.saveAttachment(attachment);
        return "redirect:/chapter6/task/getform/" + taskId;
    }

    /**
     * 删除附件
     */
    @RequestMapping(value = "delete/{attachmentId}")
    @ResponseBody
    public String delete(@PathVariable("attachmentId") String attachmentId) {
        taskService.deleteAttachment(attachmentId);
        return "true";
    }

    /**
     * 下载附件
     *
     * @throws IOException
     */
    @RequestMapping(value = "download/{attachmentId}")
    public void downloadFile(@PathVariable("attachmentId") String attachmentId, HttpServletResponse response) throws IOException {
        Attachment attachment = taskService.getAttachment(attachmentId);
        InputStream attachmentContent = taskService.getAttachmentContent(attachmentId);
        String contentType = StringUtils.substringBefore(attachment.getType(), ";");
        response.addHeader("Content-Type", contentType + ";charset=UTF-8");
        String extensionFileName = StringUtils.substringAfter(attachment.getType(), ";");
        String fileName = attachment.getName() + "." + extensionFileName;
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        IOUtils.copy(new BufferedInputStream(attachmentContent), response.getOutputStream());
    }
}
