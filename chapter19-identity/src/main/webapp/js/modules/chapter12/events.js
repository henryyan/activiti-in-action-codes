
/**
 * 读取事件列表
 * @return {[type]} [description]
 */
function readComments() {
	$('#commentList ol').html('');
	// 读取意见
	$.getJSON(ctx + '/chapter9/comment/list', {
		processInstanceId: processInstanceId,
		taskId: taskId
	}, function(datas) {
		$.each(datas.events, function(i, v) {
			$('<li/>', {
				html: function() {
					var user = (v.userId || '');
					if(user) {
						user = "<span style='margin-right: 1em;'><b>" + user + "</b></span>"
					}
					var msg = v.message || v.fullMessage;
					var content = eventHandler[v.action](v, user, msg);
					var taskName = datas.taskNames ? datas.taskNames[v.taskId] : '';
					content += "<span style='margin-left:1em;'></span>";

					// 名称不为空时才显示
					if(taskName) {
						content += "（<span class='text-info'>" + taskName + "</span>）";
					}

					content += "<span class='text-muted'>" + new Date(v.time).toLocaleString() + "</span>";
					return content;
				}
			}).appendTo('#commentList ol');
		});

	});
}

/*
根据英文类型翻译为中文
 */
function translateType(event) {
	var types = {
		"1": "贡献人",
		"2": "项目经理",
		"3": "总经理",
		"4": "业务顾问",
		"5": "技术顾问",
		"6": "执行人",
		"owner": "拥有人",
		"candidate": "候选"
	};

	var type = (types[event.messageParts[1]] || '');
	if(type == '候选') {
		if(event.action.indexOf('User') != -1) {
			return '候选人';
		} else {
			return '候选组';
		}
	}
	return type;
}

/*
事件处理器
 */
var eventHandler = {
	'DeleteAttachment': function(event, user, msg) {
		return user + '<span class="text-error">删除</span>了附件：' + msg;
	},
	'AddAttachment': function(event, user, msg) {
		return user + '添加了附件：' + msg;
	},
	'AddComment': function(event, user, msg) {
		return user + '发表了意见：' + msg;
	},
	'DeleteComment': function(event, user, msg) {
		return user + '<span class="text-error">删除</span>了意见：' + msg;
	},
	AddUserLink: function(event, user, msg) {
		return user + '邀请了<span class="text-info">' + event.messageParts[0] + '</span>作为任务的[<span class="text-info">' + translateType(event) + '</span>]';
	},
	DeleteUserLink: function(event, user, msg) {
        return user + '<span class="text-error">取消了</span><span class="text-info">' + event.messageParts[0] + '</span>的[<span class="text-info">' + translateType(event) + '</span>]角色';
	},
	AddGroupLink: function(event, user, msg) {
		return user + '添加了[<span class="text-info">' + translateType(event) + ']</span>' + event.messageParts[0];
	},
	DeleteGroupLink: function(event, user, msg) {
		return user + '从[<span class="text-info">' + translateType(event) + '</span>]中<span class="text-error">移除了</span><span class="text-info">' + event.messageParts[0] + '</span>';
	}
}