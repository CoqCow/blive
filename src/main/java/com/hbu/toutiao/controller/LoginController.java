package com.hbu.toutiao.controller;


import com.hbu.toutiao.service.MailServicee;
import com.hbu.toutiao.service.UserService;
import com.hbu.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    @Autowired
    MailServicee mailServicee;

/*    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        System.out.println("执行到注册了……");
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }*/

    @RequestMapping(path = {"/register/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public  Map<String,Object> register(Model model, @RequestParam("mail") String mail,
                           @RequestParam("nickname") String nickname,
                      @RequestParam("password") String password,
                      @RequestParam("code") String code,
                      HttpServletResponse response) {
        System.out.println("执行到注册了……"+mail);
        System.out.println("执行到注册了……"+nickname);
        System.out.println("执行到注册了……"+code);
        System.out.println("执行到注册了……"+password);

          return  userService.register(mail,code,password);

    }

    //发送邮件获取验证码
    @RequestMapping(path = {"/verifyCode/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> verifyCode(@RequestParam("mail") String to){
        return mailServicee.sendMail(to);
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
/*                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setActorId((int) map.get("userId"))
                        .setExt("username", username).setExt("email", "zjuyxy@qq.com"));*/
                return ToutiaoUtil.getJSONString(0, "成功");
            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
