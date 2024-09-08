package wiki.zhr.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserDeleteRequest
 * @Description TODO
 * @Author hrz
 * @Date 2024/9/8 14:18
 **/
@Data
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = -5275253743061986865L;

    private long id;
}
