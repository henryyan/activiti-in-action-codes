/**
 * 流程跟踪Javascript实现
 */
$(function () {

    /**
     * 获取元素的outerHTML
     */
    $.fn.outerHTML = function () {

        // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
        return (!this.length) ? this : (this[0].outerHTML ||
            (function (el) {
                var div = document.createElement('div');
                div.appendChild(el.cloneNode(true));
                var contents = div.innerHTML;
                div = null;
                return contents;
            })(this[0]));

    };

    if ($('#processDiagram').length == 1) {
        showActivities();
    }

    // 解决坐标错误问题
    $('#changeToAutoDiagram').click(function() {
        $('.activity-attr,.activity-attr-border').remove();
        $('#processDiagram').attr('src', ctx + '/chapter13/process/trace/data/auto/' + processInstanceId);
    });

});

function showActivities() {
    $.getJSON(ctx + '/chapter13/process/trace/data/' + executionId, function (infos) {

        var positionHtml = "";

        var diagramPositon = $('#processDiagram').position();
        var varsArray = new Array();
        $.each(infos, function (i, v) {
            var $positionDiv = $('<div/>', {
                'class': 'activity-attr'
            }).css({
                    position: 'absolute',
                    left: (v.x - 1),
                    top: (v.y - 1),
                    width: (v.width - 2),
                    height: (v.height - 2),
                    backgroundColor: 'black',
                    opacity: 0
                });

            // 节点边框
            var $border = $('<div/>', {
                'class': 'activity-attr-border'
            }).css({
                    position: 'absolute',
                    left: (v.x - 1),
                    top: (v.y - 1),
                    width: (v.width - 4),
                    height: (v.height - 3)
                });

            if (v.currentActiviti) {
                $border.css({
                    border: '3px solid red'
                }).addClass('ui-corner-all-12');
            }
            positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
            varsArray[varsArray.length] = v.vars;
        });

        $(positionHtml).appendTo('body').find('.activity-attr-border');

        // 鼠标移动到活动上提示
        $('.activity-attr-border').each(function (i, v) {
            var tipContent = "<table class='table table-bordered'>";
            $.each(varsArray[i], function(varKey, varValue) {
                if (varValue) {
                    tipContent += "<tr><td>" + varKey + "</td><td>" + varValue + "</td></tr>";
                }
            });
            tipContent += "</table>";
            $(this).data('vars', varsArray[i]).data('toggle', 'tooltip').data('placement', 'bottom').data('title', '活动属性').attr('title', tipContent);
        }).tooltip();
    });
}