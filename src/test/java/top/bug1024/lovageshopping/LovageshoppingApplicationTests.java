package top.bug1024.lovageshopping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.bug1024.lovageshopping.common.response.BaseResponse;
import top.bug1024.lovageshopping.common.util.MessageUtil;
import top.bug1024.lovageshopping.modules.login.entity.User;
import top.bug1024.lovageshopping.modules.login.service.UserService;

@SpringBootTest
class LovageshoppingApplicationTests {

    @Autowired
    private UserService userService;


    //测试登录
    @Test
    void contextLoads() {
        String id = "17585321151";
        String password = "4444";
        BaseResponse<User> response = new BaseResponse<>();

        if (id!=null) {
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
                return;
            }
        }
        response.setCode(503);
        response.setMessage("用户名或密码错误");
    }


}
