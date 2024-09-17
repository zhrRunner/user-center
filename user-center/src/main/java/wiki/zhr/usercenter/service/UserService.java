package wiki.zhr.usercenter.service;

import wiki.zhr.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author hrz
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-09-07 10:12:46
*/
public interface UserService extends IService<User> {

    /*
     * @Author Zou hr
     * @Description 用户注册
     * @Date 13:28 2024/9/7
     * @Param [userAccount, userPassword, checkPassword]
     * @return long 新用户id
     **/
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /*
     * @Author Zou hr
     * @Description 用户注册
     * @Date 16:19 2024/9/7
     * @Param [userAccount, userPassword]
     * @return User 返回脱敏后的用户信息
     **/
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /*
     * @Author Zou hr
     * @Description 管理员接口，搜索用户
     * @Date 13:28 2024/9/8
     * @Param [username]
     * @return java.util.List<wiki.zhr.usercenter.model.domain.User>
     **/
    List<User> searchUsers(String username);

    /*
     * @Author Zou hr
     * @Description 管理员接口，删除用户
     * @Date 13:53 2024/9/8
     * @Param [id]
     * @return void
     **/
    boolean deleteUser(long id);

    /*
     * @Author Zou hr
     * @Description 用户脱敏
     * @Date 15:33 2024/9/8
     * @Param [originUser]
     * @return wiki.zhr.usercenter.model.domain.User
     **/
    User getSafetyUser(User originUser);

    /*
     * @Author Zou hr
     * @Description 判断是否为管理员
     * @Date 14:21 2024/9/10
     * @Param [request]
     * @return boolean
     **/
    boolean isAdmin(HttpServletRequest request);
    
    /*
     * @Author Zou hr
     * @Description 退出登录
     * @Date 14:21 2024/9/10
     * @Param 
     * @return 
     **/
     Integer userLogout(HttpServletRequest request);


    /*
     * @Author Zou hr
     * @Description 管理员重置用户密码
     * @Date 16:36 2024/9/17
     * @Param [id]
     * @return boolean
     **/
    boolean resetPassword(Long id);
}
