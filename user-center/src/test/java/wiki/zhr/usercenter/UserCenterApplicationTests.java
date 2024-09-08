package wiki.zhr.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void digestUtilsTest(){
        String newPassword = DigestUtils.md5DigestAsHex("abc123456".getBytes());
        System.out.println(newPassword);

    }

    @Test
    void contextLoads() {
    }

}
