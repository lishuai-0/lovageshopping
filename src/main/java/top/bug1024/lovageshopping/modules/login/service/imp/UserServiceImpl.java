package top.bug1024.lovageshopping.modules.login.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bug1024.lovageshopping.modules.login.dao.UserDao;
import top.bug1024.lovageshopping.modules.login.entity.User;
import top.bug1024.lovageshopping.modules.login.service.UserService;

import java.util.Random;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByMail(String mail) {
        return userDao.getUserByMail(mail);
    }

    @Override
    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    @Override
    public void addUser(String phone,String loginPassword,String payPassword) {
        User user = new User();
        user.setLoginPassword(loginPassword);
        user.setPayPassword(payPassword);
        user.setUserMobile(phone);
        userDao.addUser(user);
    }
}
