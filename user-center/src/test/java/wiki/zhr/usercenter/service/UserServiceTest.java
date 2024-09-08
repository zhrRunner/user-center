package wiki.zhr.usercenter.service;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.zhr.usercenter.model.domain.User;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;



/*
 * @Author Zou hr
 * @Description 用户服务测试
 * @Date 10:25 2024/9/7
 **/
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("zouhr");
        user.setUserAccount("123456");
        user.setAvatarUrl("https://zhr-blog.oss-cn-beijing.aliyuncs.com/blog/202408201057226.jpeg");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("1733434201");
        user.setEmail("zouhaoran23@mails.ucas.ac.cn");
        user.setUserStatus(0);
        user.setIsDelete(0);
        user.setUserRole(1);
        user.setPlanetCode("1");


        boolean result = userService.save(user);
        System.out.print("该用户的id：" + user.getId());
        assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "ten der";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println(result);
        assertEquals(-1, result);
    }
}