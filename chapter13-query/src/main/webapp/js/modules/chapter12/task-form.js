$(function() {
	$('.datepicker').datepicker();
	$('#tabs').tab();

	// 读取事件列表
	readComments();

	// 保存意见
	$('#saveComment').click(function() {
		if(!$('#comment').val()) {
			return false;
		}
		$.post(ctx + '/chapter9/comment/save', {
			taskId: taskId,
			processInstanceId: processInstanceId,
			message: $('#comment').val()
		}, function(resp) {
			readComments();
		});
	});

	// 处理任务属性
	taskProperties();

	// 参与人（添加、删除）
	participants();

	// 候选人
	candidate();

	// 子任务
	subTask();

	// 附件
	attachment();

    // 任务委派
    delegate();
});

/*
任务属性更改
 */
function taskProperties() {

	// 单击到期日属性可以编辑
	$('.due-date').click(function() {
		$(this).hide();
		$('.due-date-input').show();
	});

	// 更改到期日
	$('.due-date-input').blur(function() {
		var ele = this;
		var value = $(this).val();
		if(value) {
			$.post(ctx + '/chapter6/task/property/' + taskId, {
				propertyName: 'dueDate',
				value: value
			}, function(resp) {
				if(resp == 'success') {
					$(ele).hide();
					$('.due-date').show().text(value);
				} else {
					alert(resp);
				}
			});
		}
	});

	// 单击优先级可以编辑
	$('.priority').click(function() {
		$(this).hide();
		$('#priority').show();
	});

	// 更改任务优先级
	$('#priority').change(function() {
		var ele = this;
		var value = $(this).val();
		if(value) {
			$.post(ctx + '/chapter6/task/property/' + taskId, {
				propertyName: 'priority',
				value: value
			}, function(resp) {
				if(resp == 'success') {
					$(ele).hide();
					$('.priority').show().text($('option:selected', ele).text());
				} else {
					alert(resp);
				}
			});
		}
	});

	// 单击拥有人可以编辑
	$('#owner').click(function() {
		$(this).hide();
		$('#ownerSelect').show();
	});

	// 更改任务拥有人
	$('#ownerSelect').change(function() {
		var ele = this;
		var value = $(this).val();
		if(value) {
			$.post(ctx + '/chapter6/task/property/' + taskId, {
				propertyName: 'owner',
				value: value
			}, function(resp) {
				if(resp == 'success') {
					$(ele).hide();
					$('#owner').show().text($('option:selected', ele).val());
				} else {
					alert(resp);
				}
			});
		}
	});

	// 单击办理人可以编辑
	$('#assignee').click(function() {
		$(this).hide();
		$('#assigneeSelect').show();
	});

	// 更改任务办理人
	$('#assigneeSelect').change(function() {
		var ele = this;
		var value = $(this).val();
		if(value) {
			$.post(ctx + '/chapter6/task/property/' + taskId, {
				propertyName: 'assignee',
				value: value
			}, function(resp) {
				if(resp == 'success') {
					$(ele).hide();
					$('#assignee').show().text($('option:selected', ele).val());
				} else {
					alert(resp);
				}
			});
		}
	});
}

/*
参与人
 */
function participants() {
	// 添加一个参与人
	$('.link-add').click(function() {
		/*
		实现原理：从第一行复制
		 */
		var firstTr = $('#participantsTable tbody tr:first');
		var newTr = $('<tr/>', {
			html: firstTr.html()
		}).appendTo('#participantsTable tbody');
		newTr.find('td:first').html($('#participantsTable tbody tr').length);
	});

	// 从临时列表移除一个参与人
	$('.link-remove').live('click', function() {
		$(this).parents('tr').remove();
		// 重新编号
		$('#participantsTable tbody tr').each(function(i, v) {
			$(this).find('td:first').html(i + 1);
		});
	});

	// 发送添加参与人请求
	$('#sendAddParticipantsRequest').click(function() {
		// 把临时列表的数据封装成数组
		var userIds = new Array();
		var types = new Array();

		$('#participantsTable tbody tr').each(function() {
			userIds.push($(this).find('.user option:selected').val());
			types.push($(this).find('.link-type option:selected').val());
		});
		$.post(ctx + '/chapter6/task/participant/add/' + taskId, {
			userId: userIds,
			type: types
		}, function(resp) {
			if(resp == 'success') {
				location.reload();
			} else {
				alert(resp);
			}
		});
	});

	// 从数据库删除参与人
	$('.link-delete').click(function() {
		var ele = this;
		$.post(ctx + '/chapter6/task/participant/delete/' + taskId, {
			userId: $(ele).data('userid'),
			groupId: $(ele).data('groupid'),
			type: $(ele).data('type')
		}, function(resp) {
			if(resp == 'success') {
				$(ele).parents('li').remove();
			} else {
				alert(resp);
			}
		});
	});
}

/*
候选人、候选组
 */
function candidate() {
	// 更改候选类型时切换人、组下拉框
	$('.candidateType').live('change', function() {
		var type = $(this).find('option:selected').val();
		if(type == 'user') {
			$(this).parents('tr').find('.user').show();
			$(this).parents('tr').find('.group').hide();
		} else {
			$(this).parents('tr').find('.user').hide();
			$(this).parents('tr').find('.group').show();
		}
	});

	// 添加一个参与人
	$('.candidate-add').click(function() {
		/*
		实现原理：从第一行复制
		 */
		var firstTr = $('#candidateTable tbody tr:first');
		var newTr = $('<tr/>', {
			html: firstTr.html()
		}).appendTo('#candidateTable tbody');
		newTr.find('td:first').html($('#candidateTable tbody tr').length);
	});

	// 发送添加参与人请求
	$('#sendAddCandidateRequest').click(function() {
		// 把临时列表的数据封装成数组
		var userOrGroupIds = new Array();
		var types = new Array();

		$('#candidateTable tbody tr').each(function() {
			var type = $(this).find('.candidateType option:selected').val();
			types.push(type);
			userOrGroupIds.push($(this).find('.' + type + ' option:selected').val());
		});
		$.post(ctx + '/chapter6/task/candidate/add/' + taskId, {
			userOrGroupIds: userOrGroupIds,
			type: types
		}, function(resp) {
			if(resp == 'success') {
				location.reload();
			} else {
				alert(resp);
			}
		});
	});
}

/*
子任务
 */
function subTask() {
	$('.subtask-delete').click(function() {
		if(!confirm('确定要删除任务[' + $(this).prev().text() + ']？')) {
			return;
		}
		location.href = ctx + '/chapter6/task/delete/' + $(this).data('taskid');
	});
}

/*
附件
 */
function attachment() {
	$('.attachment-item').tooltip();
	$('.attachment-delete').click(function() {
		var ele = this;
		if(confirm('确定删除附件？')) {
			$.get(ctx + '/chapter12/attachment/delete/' + $(this).data('id'), function(resp) {
				if(resp == 'true') {
					$(ele).parent().remove();
				} else {
					alert('删除失败！');
				}
			});
		}
	});
}

/*
任务委派
 */
function delegate() {
    // 单击委派人可以
    $('#delegateState').click(function() {
        $(this).hide();
        $('#delegateUserSelect').show();
    });

    // 委派任务
    $('#delegateUserSelect').change(function() {
        var ele = this;
        var value = $(this).val();
        if(value) {
            $.post(ctx + '/chapter6/task/delegate/' + taskId, {
                delegateUserId: value
            }, function(resp) {
                if(resp == 'success') {
                    location.reload();
                } else {
                    alert(resp);
                }
            });
        }
    });
}