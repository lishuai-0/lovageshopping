package top.bug1024.lovageshopping.modules.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.bug1024.lovageshopping.modules.login.entity.User;


public interface UserService extends IService<User> {
    public User getUserById(int id);
    public User getUserByMail(String mail);
    public User getUserByPhone(String phone);
    public void addUser(String phone,String loginPassword,String payPassword);
}
