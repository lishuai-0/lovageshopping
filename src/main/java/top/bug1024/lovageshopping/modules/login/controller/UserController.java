/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package top.bug1024.lovageshopping.modules.login.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.bug1024.lovageshopping.common.response.BaseResponse;
import top.bug1024.lovageshopping.common.util.MessageUtil;
import top.bug1024.lovageshopping.modules.login.entity.User;
import top.bug1024.lovageshopping.modules.login.redis.RedisDao;
import top.bug1024.lovageshopping.modules.login.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {




    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    @Resource
    private HttpServletRequest request;

    @RequestMapping("/login")
    public BaseResponse login(@RequestParam Map<String, Object> params) {
        String id = (String) params.get("id");
        String password = (String) params.get("password");
        BaseResponse<User> response = new BaseResponse<>();

        if (id!=null&&id.equals("")) {
            User user = null;
            if (id.length() == 11){//手机号登录
                user = userService.getUserByPhone(id);

            }else if (MessageUtil.checkEmaile(id)){//邮箱登录
                user = userService.getUserByMail(id);
            }else{//用户id登录
                user = userService.getUserById(Integer.valueOf(id));
            }
            if (user!=null && user.getLoginPassword().equals(password)){
                response.setCode(200);
                response.setData(user);
                response.setMessage("登陆成功");
                //保存用户登录状态
                redisDao.set("useId_"+user.getUserId(),user);
                return response;
            }
        }
        response.setCode(503);
        response.setMessage("用户名或密码错误");
        return response;
    }

    @RequestMapping("/getUser")
    public BaseResponse getUser(@RequestParam Map<String, Object> params) {
        String id = (String) params.get("id");
        BaseResponse<User> response = new BaseResponse<>();
        if (id!=null&&!id.equals("")) {
            User user = (User) redisDao.get("useId_"+id);
            response.setCode(200);
            response.setData(user);
            response.setMessage("获取用户信息成功");
            return response;
        }
        response.setCode(503);
        response.setMessage("获取用户信息失败");
        return response;
    }


    @RequestMapping("/reset")
    public BaseResponse resetPaaword(@RequestParam Map<String, Object> params){
        String phone = (String) params.get("phone");
        String loginPassword = (String) params.get("loginPassword");
        String payPassword = (String) params.get("payPassword");
        String code = (String) params.get("code");
        BaseResponse<String> response = new BaseResponse<>();
        if (phone!=null && code!=null){
            //获取缓存验证码
            String oldCode = (String) redisDao.get(phone+"Reset");
            if (code.equals(oldCode)){//验证码相同

                User user = userService.getUserByPhone(phone);
                if (user==null){//用户未注册
                    response.setCode(506);
                    response.setMessage("手机号未注册");
                    return response;
                }
                //将用户新信息到数据库中
                user.setLoginPassword(loginPassword);
                user.setPayPassword(payPassword);
                if (userService.updateById(user)){
                    response.setCode(200);
                    response.setMessage("修改成功");
                    return response;
                }

            }else{
                response.setCode(504);
                response.setMessage("验证码错误");
                return response;
            }
        }
        response.setCode(505);
        response.setMessage("未知异常");
        return response;
    }

    @RequestMapping("/toRegister")
    public BaseResponse register(@RequestParam Map<String, Object> params){
        String phone = (String) params.get("phone");
        String loginPassword = (String) params.get("loginPassword");
        String payPassword = (String) params.get("payPassword");
        String code = (String) params.get("code");
        BaseResponse<String> response = new BaseResponse<>();
        if (phone!=null && code!=null){
            //获取缓存验证码
            String oldCode = (String) redisDao.get(phone);
            if (code.equals(oldCode)){//验证码相同
                //将用户添加到数据库中
                //添加到数据库中
                User user = userService.getUserByPhone(phone);
                if (user!=null){//手机号已注册
                    response.setCode(503);
                    response.setMessage("手机号已注册");
                    return response;
                }
                User newUser = new User(phone,loginPassword,payPassword,new Date(),new Date());
                userService.save(newUser);
                response.setCode(200);
                response.setMessage("注册成功");
                return response;
            }else{
                response.setCode(504);
                response.setMessage("验证码错误");
                return response;
            }
        }
        return response;
    }

    @RequestMapping("/phone")
    public BaseResponse sendByPhone(@RequestParam Map<String, Object> params) {
        String phone = (String) params.get("phone");
        String str = (String) params.get("num");
        BaseResponse<String> res = new BaseResponse<>();
        Integer num = null;
        if (str!=null){
            if (str.length()>10){
                res.setCode(504);
                res.setMessage("验证码长度过长");
                return res;
            }
            str = (String) params.get("num");
            num=Integer.valueOf(str) ;
        }
        if (phone!=null) {
            if (MessageUtil.checkPhone(phone)){//是一个手机号
                //可从session里取，娶不到再发送，作延时获取验证码
                res.setCode(200);
                res.setMessage("发送成功");
                String result = MessageUtil.sendMsgByPhone(phone,num ==null ?6:num);
                //将手机号和验证码存到redis里面
                redisDao.set(phone,result);
                return res;
            }else{//不是手机号
                res.setCode(503);
                res.setMessage("手机号不正确");
            }
        }else {
            res.setCode(504);
            res.setMessage("手机号为空");
        }
        return res;
    }

    @RequestMapping("/reset/phone")
    public BaseResponse sendByPhoneReset(@RequestParam Map<String, Object> params) {
        String phone = (String) params.get("phone");
        String str = (String) params.get("num");
        BaseResponse<String> res = new BaseResponse<>();
        Integer num = null;
        if (str!=null){
            if (str.length()>10){
                res.setCode(504);
                res.setMessage("验证码长度过长");
                return res;
            }
            str = (String) params.get("num");
            num=Integer.valueOf(str) ;
        }
        if (phone!=null) {
            if (MessageUtil.checkPhone(phone)){//是一个手机号
                //可从session里取，娶不到再发送，作延时获取验证码
                res.setCode(200);
                res.setMessage("发送成功");
                String result = MessageUtil.sendMsgByPhone(phone,num ==null ?6:num);
                //将手机号和验证码存到session里面
                redisDao.set(phone+"Reset",result);
                return res;
            }else{//不是手机号
                res.setCode(503);
                res.setMessage("手机号不正确");
            }
        }else {
            res.setCode(504);
            res.setMessage("手机号为空");
        }
        return res;
    }


    @RequestMapping("/mail")
    public BaseResponse sendByMail(@RequestParam Map<String, Object> params) {
        String address = (String) params.get("address");
        String str = (String) params.get("num");
        BaseResponse<String> res = new BaseResponse<>();
        Integer num = null;
        if (str!=null){
            if (str.length()>10){
                res.setCode(504);
                res.setMessage("验证码长度过长");
                return res;
            }
            str = (String) params.get("num");
            num=Integer.valueOf(str) ;
        }
        if (address!=null) {
            if (MessageUtil.checkEmaile(address)){//是一个手机号
                //可从session里取，娶不到再发送，作延时获取验证码
                res.setCode(200);
                res.setMessage("发送成功");
                String result = MessageUtil.sendMsgByMail(address,num ==null ?6:num);
                //将手机号和验证码存到session里面
                redisDao.set(address,result);
            }else{//不是手机号
                res.setCode(503);
                res.setMessage("邮箱格式不正确");
            }
        }else {
            res.setCode(504);
            res.setMessage("手机号为空");
        }
        return res;
    }
}
