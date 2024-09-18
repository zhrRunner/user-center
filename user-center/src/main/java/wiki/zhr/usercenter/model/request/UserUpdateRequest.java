package wiki.zhr.usercenter.model.request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserUpdateRequest
 * @Description 管理员更新用户信息请求体
 * @Author hrz
 * @Date 2024/9/18 15:59
 **/
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = -4848541494680305061L;
    private String username;

    private String userAccount;

    private String avatarUrl;

    private Integer gender;

    private String phone;

    private String email;

    private Integer userStatus;

    private Integer userRole;
}
