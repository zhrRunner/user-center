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
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 4130327089789899216L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
