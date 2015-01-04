package me.kafeitu.activiti.chapter7.spring.boot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HelloController {

    /**
     * 输出菜单
     * @return
     */
    @RequestMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

}