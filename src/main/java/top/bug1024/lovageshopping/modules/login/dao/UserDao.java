package top.bug1024.lovageshopping.modules.login.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.bug1024.lovageshopping.modules.login.entity.User;

@Mapper
public interface UserDao extends BaseMapper<User> {

    public User getUserById(int userId);
    public User getUserByMail(String userMail);
    public User getUserByPhone(String userMobile);
    public void addUser(User user);
}
