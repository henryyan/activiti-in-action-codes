<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.form.*, org.apache.commons.lang3.*" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>任务办理</title>
	<style type="text/css">
	.task-infos {margin-left: 20px;}
	.task-infos span {margin-right: 20px;}
	#commentList span {padding: 0 5px 0 5px;}
	.attachment-delete {margin-left: 2em;}
	</style>

	<script type="text/javascript" src="${ctx }/js/common/jquery.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		var taskId = '${task.id}';
		var processInstanceId = '${task.processInstanceId}';
	</script>
	<script type="text/javascript" src="${ctx }/js/modules/chapter12/events.js"></script>
	<script type="text/javascript" src="${ctx }/js/modules/chapter12/task-form.js"></script>
</head>
<body>
    <c:if test="${not empty message}">
        <div id="message" class="alert alert-success">${message}</div>
        <!-- 自动隐藏提示信息 -->
        <script type="text/javascript">
            setTimeout(function() {
                $('#message').hide('slow');
            }, 5000);
        </script>
    </c:if>
    <c:if test="${not empty error}">
        <div id="error" class="alert alert-error">${error}</div>
        <!-- 自动隐藏提示信息 -->
        <script type="text/javascript">
            setTimeout(function() {
                $('#error').hide('slow');
            }, 5000);
        </script>
    </c:if>

	<h3>任务办理—[${task.name}]
		<c:if test="${not empty task.processDefinitionId}">
			，流程定义ID：[${task.processDefinitionId}]
		</c:if>
	</h3>
	<!-- 任务属性 -->
	<p class="task-infos text-left">
		<span><i class="icon-calendar"></i>到期日：
			<span class='due-date'>
				<c:if test="${empty task.dueDate}">无到期日</c:if>
				<c:if test="${not empty task.dueDate}">
					<fmt:formatDate value="${task.dueDate}" pattern="yyyy-MM-dd"/>
				</c:if>
			</span>
			<input type="text" style="display:none" class='due-date-input datepicker input-small' data-date-format="yyyy-mm-dd"/>
		</span>
		<span><i class="icon-flag"></i>优先级：
			<span class='priority'>
				<c:if test="${empty task.priority}">无到期日</c:if>
				<c:if test="${not empty task.priority}">
					<c:if test="${task.priority == 0}">低</c:if>
					<c:if test="${task.priority == 50}">中</c:if>
					<c:if test="${task.priority == 100}">高</c:if>
				</c:if>
			</span>
			<select name="priority" id="priority" style="display:none;width:50px;">
				<option value="0">低</option>
				<option value="50">中</option>
				<option value="100">高</option>
			</select>
		</span>
		<span><i class="icon-calendar"></i>创建日期：
			<fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd hh:mm:ss"/>
		</span>
		<c:if test="${not empty parentTask}">
			<hr/>
			<%-- 运行中任务 --%>
			<c:if test="${empty parentTask.endTime}">
				<div>
					该任务属于：<a href="${ctx}/chapter6/task/getform/${parentTask.id}">${parentTask.name}</a>
				</div>
			</c:if>

			<%-- 已结束任务 --%>
			<c:if test="${not empty parentTask.endTime}">
				<div>
					该任务属于：<a href="${ctx}/chapter6/task/archived/${parentTask.id}">${parentTask.name}</a>
				</div>
			</c:if>
		</c:if>
	</p>
	<hr/>
	<%-- 任务描述 --%>
	<c:if test="${empty task.description}">
		<p class="text-muted">该任务无描述。</p>
	</c:if>
	<c:if test="${not empty task.description}">
		<p class="text-infos">${task.description}</p>
	</c:if>

	<hr/>
	<p class="task-infos text-left">
		<h4 style="display:inline;">任务人员</h4>
		<a href="#addPeopleModal" class="btn btn-small btn-success" data-toggle="modal" style="margin-left: 4em;"><i class="icon-plus"></i>邀请参与人</a>

		<%-- 添加候选人/组只能针对未签收的任务 --%>
		<c:if test="${empty task.assignee}">
			<a href="#addCandidateModal" class="btn btn-small btn-success" data-toggle="modal" style="margin-left: 4em;"><i class="icon-plus"></i>添加候选[人|组]</a>
		</c:if>

		<div class="row">
			<div class="span4">
				<span><i class="icon-user"></i>拥有人：<span id="owner">${empty task.owner ? '无' : task.owner}</span></span>
				<select id="ownerSelect" style="display:none;width:100px;">
					<c:forEach items="${users}" var="user">
						<option value="${user.id}">${user.firstName} ${user.lastName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="span4">
				<span><i class="icon-user"></i>办理人：<span id="assignee">${empty task.assignee ? '无' : task.assignee}</span></span>
				<select id="assigneeSelect" style="display:none;width:100px;">
					<c:forEach items="${users}" var="user">
						<option value="${user.id}">${user.firstName} ${user.lastName}</option>
					</c:forEach>
				</select>
			</div>

            <div class="span4">
                <%-- 未委派 --%>
                <c:if test="${empty task.delegationState}">
                    <span><i class="icon-user"></i>任务委派：<span id="delegateState">${empty task.delegationState ? '无' : task.delegationState }</span></span>
                </c:if>

                <%-- 已委派 --%>
                <c:if test="${not empty task.delegationState}">
                    <i class="icon-user"></i>任务委派：
                    <span id="delegateState">
                    <c:if test="${task.delegationState == 'PENDING' }">
                        <i class="icon-ok"></i>被委派
                    </c:if>
                    <c:if test="${task.delegationState == 'RESOLVED' }">
                        <i class="icon-ok"></i>任务已处理完成
                    </c:if>
                    </span>
                </c:if>
                <select id="delegateUserSelect" style="display:none;width:100px;">
                    <c:forEach items="${users}" var="user">
                        <option value="${user.id}">${user.firstName} ${user.lastName}</option>
                    </c:forEach>
                </select>
            </div>
		</div>

		<!-- 读取参与人\候选[人|组] -->
		<div class="row">
			<div class="span"><p style="padding-top: 1em;" class="text-info text-center">参与人<br/>候选[人|组]</p></div>
			<div class="span10">
				<ul style="margin-top: 1em;">
				<c:forEach items="${identityLinksForTask}" var="link">
					<c:set var="type" value="" />
					<c:choose>
						<c:when test="${link.type == 'candidate'}"><c:set var="type" value="候选${not empty link.userId ? '人' : '组'}" /></c:when>
						<c:when test="${link.type == '1'}"><c:set var="type" value="贡献人（参与人）" /></c:when>
						<c:when test="${link.type == '2'}"><c:set var="type" value="项目经理（参与人）" /></c:when>
						<c:when test="${link.type == '3'}"><c:set var="type" value="总经理（参与人）" /></c:when>
						<c:when test="${link.type == '4'}"><c:set var="type" value="业务顾问（参与人）" /></c:when>
						<c:when test="${link.type == '5'}"><c:set var="type" value="技术顾问（参与人）" /></c:when>
						<c:when test="${link.type == '6'}"><c:set var="type" value="执行人（参与人）" /></c:when>
					</c:choose>
					<c:if test="${not empty type}">
						<li>
							<span>${empty link.userId ? link.groupId : link.userId} -- ${type}</span>
							<a href="#" data-userid="${link.userId}" data-groupid="${link.groupId}" data-type="${link.type}" class="link-delete" style="margin-left: 2em;"><i class="icon-remove"></i>删除</a>
						</li>
					</c:if>
				</c:forEach>
				</ul>
			</div>
		</div>
	</p>
	<hr/>

	<!-- 子任务 -->
	<p class="text-left">
		<h4 style="display:inline;">子任务</h4>
		<a href="#addSubtaskModal" style="margin-left: 3em;" data-toggle="modal" class="btn btn-small btn-success"><i class="icon-plus"></i>添加</a>
		<ul class="unstyled">
			<c:forEach items="${subTasks}" var="subTask">
			<li>
				<%-- 根据任务完成状态显示不同的图标 --%>
				<i class="icon-${empty subTask.endTime ? 'tasks' : 'ok'}"></i>

				<c:if test="${empty subTask.endTime}">
					<a href="${subTask.id}" class="task-name">${subTask.name}</a>
				</c:if>

				<c:if test="${not empty subTask.endTime}">
					<a href="${ctx}/chapter6/task/archived/${subTask.id}" class="task-name">
					${subTask.name}（完成时间：<fmt:formatDate value="${subTask.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/>）</a>
				</c:if>

				<c:if test="${subTask.owner == user.id && empty subTask.endTime}">
					<a href="javascript:;" style="margin-left: 1em;" class="subtask-delete" data-taskid="${subTask.id}"><i class="icon-remove"></i>删除</a>
				</c:if>
			</li>
		</c:forEach>
		</ul>
	</p>
	<hr/>

	<!-- 附件 -->
	<div>
		<h4 style="display:inline;">相关附件</h4>
		<a href="#addAttachmentModal" style="margin-left: 3em;" data-toggle="modal" class="btn btn-small btn-success"><i class="icon-plus"></i>添加</a>
		<ul class="unstyled">
			<c:forEach items="${attachments}" var="attachment">
			<li>
				<%-- url类型的文件 --%>
				<c:if test="${attachment.type == 'url'}">
					<a href="${attachment.url}" target="_blank" data-toggle="tooltip" title="地址：${attachment.url}<br/>描述：${attachment.description}" data-placement="right" class="attachment-item"><i class="icon-share"></i>${attachment.name}</a>
				</c:if>

				<%-- 文件类型的文件 --%>
				<c:if test="${attachment.type != 'url'}">
					<a href="${ctx}/chapter12/attachment/download/${attachment.id}" target="_blank" data-toggle="tooltip" title="${attachment.description}" data-placement="right" class="attachment-item"><i class="icon-file"></i>${attachment.name}</a>
				</c:if>
				<a href="javascript:;" data-id="${attachment.id}" class="attachment-delete"><i class="icon-remove"></i>删除</a>
			</li>
		</c:forEach>
		</ul>
	</div>

	<hr/>
	<form action="${ctx }/chapter6/task/complete/${task.id}" class="form-horizontal" method="post">
		<h4>任务内容</h4>
		<c:if test="${hasFormKey}">
			${taskFormData}
		</c:if>
		<c:if test="${!hasFormKey}">
		<c:forEach items="${taskFormData.formProperties}" var="fp">
			<c:set var="fpo" value="${fp}"/>
			<c:set var="disabled" value="${fp.writable ? '' : 'disabled'}" />
			<c:set var="readonly" value="${fp.writable ? '' : 'readonly'}" />
			<c:set var="required" value="${fp.required ? 'required' : ''}" />
			<%
				// 把需要获取的属性读取并设置到pageContext域
				FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
				String[] keys = {"datePattern", "values"};
				for (String key: keys) {
					pageContext.setAttribute(key, type.getInformation(key));
				}
			%>
			<div class="control-group">
			<%-- 文本或者数字类型 --%>
			<c:if test="${fp.type.name == 'string' || fp.type.name == 'long' || fp.type.name == 'double'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" ${readonly} ${required} />
				</div>
			</c:if>

			<%-- 大文本 --%>
			<c:if test="${fp.type.name == 'bigtext'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<textarea id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" ${readonly} ${required}>${fp.value}</textarea>
				</div>
			</c:if>

			<%-- 日期 --%>
			<c:if test="${fp.type.name == 'date'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" value="${fp.value}" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" ${readonly} ${required}/>
				</div>
			</c:if>

			<%-- 下拉框 --%>
			<c:if test="${fp.type.name == 'enum'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<select name="${fp.id}" id="${fp.id}" ${disabled} ${required}>
						<c:forEach items="${values}" var="item">
							<option value="${item.key}" <c:if test="${item.value == fp.value}">selected</c:if>>${item.value}</option>
						</c:forEach>
					</select>
				</div>
			</c:if>

			<%-- Javascript --%>
			<c:if test="${fp.type.name == 'javascript'}">
				<script type="text/javascript">${fp.value};</script>
			</c:if>

			</div>
		</c:forEach>
		</c:if>

		<%-- 按钮区域 --%>
		<div class="control-group">
			<div class="controls">
				<a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
				<c:if test="${not empty task.assignee}">
					<button type="submit" class="btn btn-primary"><i class="icon-ok"></i>完成任务</button>
				</c:if>
				<c:if test="${empty task.assignee}">
					<a class="btn" href="${ctx }/chapter6/task/claim/${task.id}?nextDo=handle"><i class="icon-ok"></i>签收</a>
				</c:if>
			</div>
		</div>
	</form>

	<%-- 事件区域，参考events.js --%>
	<div class="row">
		<div class="span2">
	      <!-- 添加意见 -->
			<fieldset id="commentContainer" style="margin-left: 5em;">
				<legend>添加意见</legend>
				<textarea id="comment" rows="3"></textarea>
				<button id="saveComment" type="button" class="btn"><i class="icon-plus"></i>保存意见<i class="icon-comment"></i></button>
			</fieldset>
	    </div>
	    <div class="span10">
	    	<%-- 列表是通过js发送ajax请求后动态生成的 --%>
	      <fieldset id="commentList" style="margin-left: 12em;">
			<legend>事件列表</legend>
			<ol></ol>
		  </fieldset>
	    </div>
	</div>

	<!-- 添加参与人对话框 -->
	<div id="addPeopleModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addPeopleModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="addPeopleModalLabel">邀请参与人</h3>
	  </div>
	  <div class="modal-body">
	    <table id="participantsTable" class="table table-hover">
	    	<thead>
	    		<tr>
	    			<th><button class="btn btn-small link-add"><i class="icon-plus"></i></button></th>
	    			<th>人员</th>
	    			<th>职能</th>
	    			<th>操作</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    		<tr>
	    			<td width="10">1</td>
	    			<td>
	    				<select class='user' style="width: 130px">
							<c:forEach items="${users}" var="user">
								<option value="${user.id}">${user.firstName} ${user.lastName}</option>
							</c:forEach>
						</select>
	    			</td>
	    			<td>
	    				<select class="link-type" style="width: 100px;">
    						<option value="1">贡献人</option>
	    					<option value="2">项目经理</option>
	    					<option value="3">总经理</option>
	    					<option value="4">业务顾问</option>
	    					<option value="5">技术顾问</option>
	    					<option value="6">执行人</option>
	    				</select>
	    			</td>
	    			<td>
	    				<button class="btn btn-small link-remove"><i class="icon-remove"></i></button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	  </div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
	    <button id="sendAddParticipantsRequest" class="btn btn-primary">确定邀请</button>
	  </div>
	</div>

	<!-- 添加候选[人|组]对话框 -->
	<div id="addCandidateModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addCandidateModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="addCandidateModalLabel">添加候选[人|组]</h3>
	  </div>
	  <div class="modal-body">
	    <table id="candidateTable" class="table table-hover">
	    	<thead>
	    		<tr>
	    			<th><button class="btn btn-small candidate-add"><i class="icon-plus"></i></button></th>
	    			<th>候选类型</th>
	    			<th>人/组</th>
	    			<th>操作</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    		<tr>
	    			<td width="10">1</td>
	    			<td>
	    				<select class='candidateType' style="width: 100px">
							<option value="user">候选人</option>
							<option value="group">候选组</option>
						</select>
	    			</td>
	    			<td>
	    				<select class='user' style="width: 130px">
							<c:forEach items="${users}" var="user">
								<option value="${user.id}">${user.firstName} ${user.lastName}</option>
							</c:forEach>
						</select>
						<select class='group' style="width: 130px;display:none;">
							<c:forEach items="${groups}" var="group">
								<option value="${group.id}">${group.name}</option>
							</c:forEach>
						</select>
	    			</td>
	    			<td>
	    				<button class="btn btn-small link-remove"><i class="icon-remove"></i></button>
	    			</td>
	    		</tr>
	    	</tbody>
	    </table>
	  </div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
	    <button id="sendAddCandidateRequest" class="btn btn-primary">确定添加</button>
	  </div>
	</div>

	<!-- 添加子任务对话框 -->
	<div id="addSubtaskModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addSubtaskModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="addSubtaskModalLabel">添加子任务</h3>
	  </div>
	  <div class="modal-body">
	    <form id="subTaskForm" action="${ctx}/chapter6/task/subtask/add/${task.id}" class="form-horizontal" method="post">
	    	<div class="control-group">
		    	<label class="control-label" for="subTaskName">任务名称:</label>
				<div class="controls">
					<input type="text" name="taskName" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="subTaskName">任务描述:</label>
				<div class="controls">
					<textarea name="description" class="required"></textarea>
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			    	<button type="submit" class="btn btn-primary">确定添加</button>
			    </div>
		    </div>
	    </form>
	  </div>
	</div>

	<!-- 添加附件对话框 -->
	<div id="addAttachmentModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addAttachmentModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="addAttachmentModalLabel">添加附件</h3>
	  </div>
	  <div class="modal-body">
	  	<ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
		  <li class="active"><a data-toggle="tab" href="#file"><i class="icon-file"></i>文件</a></li>
		  <li><a data-toggle="tab" href="#web"><i class="icon-share"></i>Web资源</a></li>
		</ul>
		<div id="my-tab-content" class="tab-content">
		    <div id="file" class="tab-pane active">
		    	<form id="fileAttachmentForm" action="${ctx}/chapter12/attachment/new/file" class="form-horizontal" method="post" enctype="multipart/form-data">
		    		<input type="hidden" name="taskId" value="${task.id}" />
		    		<input type="hidden" name="processInstanceId" value="${task.processInstanceId}" />
			    	<div class="control-group">
				    	<label class="control-label" for="attachmentName">名称:</label>
						<div class="controls">
							<input type="text" name="attachmentName" class="required" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="attachmentDescription">描述:</label>
						<div class="controls">
							<textarea name="attachmentDescription" class="required"></textarea>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="attachmentDescription">文件:</label>
						<div class="controls">
							<input type="file" name="file" />
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
					    	<button type="submit" class="btn btn-primary">创建</button>
					    </div>
				    </div>
			    </form>
		    </div>

		    <div id="web" class="tab-pane">
		    	<form id="urlAttachmentForm" action="${ctx}/chapter12/attachment/new/url" class="form-horizontal" method="post">
		    		<input type="hidden" name="taskId" value="${task.id}" />
		    		<input type="hidden" name="processInstanceId" value="${task.processInstanceId}" />
					<div class="control-group">
						<label class="control-label" for="attachmentDescription">URL:</label>
						<div class="controls">
							<input type="text" name="url" />
						</div>
					</div>
			    	<div class="control-group">
				    	<label class="control-label" for="attachmentName">名称:</label>
						<div class="controls">
							<input type="text" name="attachmentName" class="required" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="attachmentDescription">描述:</label>
						<div class="controls">
							<textarea name="attachmentDescription" class="required"></textarea>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
					    	<button type="submit" class="btn btn-primary">创建</button>
					    </div>
				    </div>
			    </form>
		    </div>
		</div>

	  </div>
	</div>

</body>
</html>