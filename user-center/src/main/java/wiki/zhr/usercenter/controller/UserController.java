package wiki.zhr.usercenter.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import wiki.zhr.usercenter.constant.UserConstant;
import wiki.zhr.usercenter.model.domain.User;
import wiki.zhr.usercenter.model.request.Result;
import wiki.zhr.usercenter.model.request.UserDeleteRequest;
import wiki.zhr.usercenter.model.request.UserLoginRequest;
import wiki.zhr.usercenter.model.request.UserRegisterRequest;
import wiki.zhr.usercenter.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName UserController
 * @Description 用户控制层
 * @Author hrz
 * @Date 2024/9/7 19:51
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        if(id == -1) return Result.error("抱歉注册失败啦");
        return Result.success(id);
    }


    @PostMapping("/login")
    public User userRegister(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        if(user == null) {
//            return Result.error("账户或密码不正确～请重试");
            return null;
        }
        return user;
    }

    @GetMapping("/search")
    public Result searchUsers(String username, HttpServletRequest request){

        if(!userService.isAdmin(request)) return Result.error("您没有权限访问此接口～");

        List<User> users = userService.searchUsers(username);
        if(users.isEmpty()){
            return Result.error("未查询到用户名为" + username + "的用户");
        }
        return Result.success(users);
    }

    @PostMapping("/delete")
    public Result deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request){

        if(!userService.isAdmin(request)) return Result.error("您没有权限访问此接口～");

        boolean isFind = userService.deleteUser(userDeleteRequest.getId());
        if(isFind) {
            return Result.success();
        }
        return Result.error("删除失败，此人可能已被删除～");
    }


}
