/**
 * 首页JavaScript
 *
 * @author HenryYan
 */
$(function() {

	// 退出系统
	$('#loginOut').click(function(){
		if (confirm('系统提示，您确定要退出本次登录吗?')) {
			location.href = ctx + '/login.jsp';
		}
    });

	// 自动根据分辨率调整iframe的大小
    window.onresize = function() {
    	autoResizeIframeHeight();
    };
    window.onresize();

    // 把菜单项的链接设置到iframe的src中
    $('.nav a[rel]').click(function(){
    	$('.nav .active').removeClass('active');
    	if ($(this).parents('li').hasClass('dropdown')) {
	    	$(this).parents('.dropdown').addClass('active');
	    	$('.active > a').trigger('click');
    	} else {
    		$(this).parent().addClass('active');
    	}
    	$('iframe').attr('src', ctx + "/" + $(this).attr('rel'));
    });
});

/**
 * 自动根据分辨率调整iframe的大小
 */
function autoResizeIframeHeight() {
	$('iframe').height(document.documentElement.clientHeight - 60);
}