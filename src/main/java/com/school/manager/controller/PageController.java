package com.school.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jack
 * @date 2020/3/26 20:41
 */
@Controller
public class PageController {

    /**
     * 跳转到登录页面
     * @param mv
     * @return
     */
    @RequestMapping("/login")
    public ModelAndView login (ModelAndView mv) {
        mv.setViewName("login");
        return mv;
    }

    /**
     * 注册
     * @param mv
     * @return
     */
    @RequestMapping("/signup")
    public ModelAndView signup (ModelAndView mv) {
        mv.setViewName("admin/signup");
        return mv;
    }
    @RequestMapping("/logout")
    public ModelAndView logout (ModelAndView mv) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        mv.setViewName("login");
        return mv;
    }

    /**
     * 管理员登录成功跳转到主页面
     * @param mv
     * @return
     */
    @RequestMapping("/main")
    public ModelAndView main (ModelAndView mv) {
        mv.setViewName("index_1");
        return mv;
    }
    /**
     * 普通用户登录成功跳转到主页面
     * @param mv
     * @return
     */
    @RequestMapping("/main_1")
    public ModelAndView main_1 (ModelAndView mv) {
        mv.setViewName("main_1");
        return mv;
    }

    @RequestMapping("/")
    public ModelAndView index (ModelAndView mv) {
        mv.setViewName("blog/index");
        return mv;
    }



    @RequestMapping("/firstIndex")
    public ModelAndView welcome (ModelAndView mv) {
        mv.setViewName("welcome");
        return mv;
    }

    @RequestMapping("/welcome1")
    public ModelAndView welcome1 (ModelAndView mv) {
        mv.setViewName("welcome1");
        return mv;
    }

    @RequestMapping("/member-list")
    public ModelAndView memberList (ModelAndView mv) {
        mv.setViewName("member-list");
        return mv;
    }

    @RequestMapping("/member-add")
    public ModelAndView memberAdd (ModelAndView mv) {
        mv.setViewName("member-add");
        return mv;
    }

    @RequestMapping("/member-edit")
    public ModelAndView memberEdit (ModelAndView mv) {
        mv.setViewName("member-edit");
        return mv;
    }

    @RequestMapping("/member-password")
    public ModelAndView memberPassword (ModelAndView mv) {
        mv.setViewName("member-password");
        return mv;
    }

    @RequestMapping("/product-list")
    public ModelAndView productList (ModelAndView mv) {
        mv.setViewName("product-list");
        return mv;
    }

    @RequestMapping("/product-add")
    public ModelAndView productAdd (ModelAndView mv) {
        mv.setViewName("product-add");
        return mv;
    }

    @RequestMapping("/product-edit")
    public ModelAndView productEdit (ModelAndView mv) {
        mv.setViewName("product-edit");
        return mv;
    }

    @RequestMapping("/confirm-list")
    public ModelAndView confirmList (ModelAndView mv) {
        mv.setViewName("confirm-list");
        return mv;
    }

    @RequestMapping("/confirm-add")
    public ModelAndView confirmAdd (ModelAndView mv) {
        mv.setViewName("confirm-add");
        return mv;
    }

    @RequestMapping("/confirm-edit")
    public ModelAndView confirmtEdit (ModelAndView mv) {
        mv.setViewName("confirm-edit");
        return mv;
    }

    @RequestMapping("/collect")
    public ModelAndView collect (ModelAndView mv) {
        mv.setViewName("collect");
        return mv;
    }



}
