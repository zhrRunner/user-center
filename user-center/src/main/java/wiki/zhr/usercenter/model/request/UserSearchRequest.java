package wiki.zhr.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserSearchRequest
 * @Description 搜索用户请求体
 * @Author hrz
 * @Date 2024/9/19 13:46
 **/
@Data
public class UserSearchRequest implements Serializable {
    private static final long serialVersionUID = -8763760407014102688L;

    private String username;
    private String userAccount;
    private Integer gender;
    private String phone;
    private String email;
    private Integer userStatus;
    private Integer userRole;
}
