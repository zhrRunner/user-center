package wiki.zhr.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserRegisterRequest
 * @Description 用户注册请求体
 * @Author hrz
 * @Date 2024/9/7 20:07
 **/
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 2079833520863731824L;

    private String userAccount;

    private String userPassword;


}
