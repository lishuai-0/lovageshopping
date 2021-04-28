/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package top.bug1024.lovageshopping.common.openInterface;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bug1024.lovageshopping.common.response.BaseResponse;
import top.bug1024.lovageshopping.common.util.MessageUtil;

import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class MessageController {

    @RequestMapping("/mail")
    public BaseResponse sendByMail(@RequestParam Map<String, Object> params) {
        String address = (String) params.get("address");
        String str = (String) params.get("num");
        BaseResponse<String> res = new BaseResponse<>();
        Integer num = null;
        if (str!=null){
            if (str.length()>10){
                res.setCode(505);
                res.setMessage("验证码长度过长");
                return res;
            }
            str = (String) params.get("num");
            num=Integer.valueOf(str) ;
        }
        if (address!=null) {
            if (MessageUtil.checkEmaile(address)){//是一个邮箱
                res.setCode(200);
                res.setMessage("发送成功");
                res.setData(MessageUtil.sendMsgByMail(address,num ==null ?6:num));
            }else{//不是邮箱
                res.setCode(503);
                res.setMessage("邮箱不正确");
            }
        }else {
            res.setCode(504);
            res.setMessage("邮箱为空");
        }
        return res;
    }

    @RequestMapping("/phone")
    public BaseResponse sendByPhone(@RequestParam Map<String, Object> params) {
        String phone = (String) params.get("phone");
        String str = (String) params.get("num");
        BaseResponse<String> res = new BaseResponse<>();
        Integer num = null;
        if (str!=null){
            if (str.length()>10){
                res.setCode(505);
                res.setMessage("验证码长度过长");
                return res;
            }
            str = (String) params.get("num");
            num=Integer.valueOf(str) ;
        }


        if (phone!=null) {
            if (MessageUtil.checkPhone(phone)){//是一个手机号
                res.setCode(200);
                res.setMessage("发送成功");
                res.setData(MessageUtil.sendMsgByPhone(phone,num ==null ?6:num));
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
}
