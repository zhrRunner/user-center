package wiki.zhr.usercenter.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import wiki.zhr.usercenter.common.BaseResponse;
import wiki.zhr.usercenter.common.ErrorCode;
import wiki.zhr.usercenter.common.ResultUtils;
import wiki.zhr.usercenter.constant.UserConstant;
import wiki.zhr.usercenter.exception.BusinessException;
import wiki.zhr.usercenter.model.domain.User;
import wiki.zhr.usercenter.model.request.Result;
import wiki.zhr.usercenter.model.request.UserDeleteRequest;
import wiki.zhr.usercenter.model.request.UserLoginRequest;
import wiki.zhr.usercenter.model.request.UserRegisterRequest;
import wiki.zhr.usercenter.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName UserController
 * @Description 用户控制层
 * @Author hrz
 * @Date 2024/9/7 19:51
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
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
//        if(id == -1) return Result.error("抱歉注册失败啦");
        if(id == -1) return null;
        return id;
    }


    @PostMapping("/login")
    public User useLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
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

//    @GetMapping("/search")
//    public Result searchUsers(String username, HttpServletRequest request){
//
//        if(!userService.isAdmin(request)) return Result.error("您没有权限访问此接口～");
//
//        List<User> users = userService.searchUsers(username);
//        if(users.isEmpty()){
//            return Result.error("未查询到用户名为" + username + "的用户");
//        }
//        return Result.success(users);
//    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> users = userService.searchUsers(username);
        log.info("查询到了{}个用户", users.size());
        return ResultUtils.success(users);
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

    @GetMapping("/current")
    public User getCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            return null;
        }
        // 这里不直接从session缓存里查询而是走数据库
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    @PostMapping("/outlogin")
    public void outLogin(HttpServletRequest request) {
        // 获取当前 session
        HttpSession session = request.getSession();

        // 移除 session 中的用户登录状态
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);

        // 如果需要销毁整个 session，可以使用 session.invalidate()
        // session.invalidate();
    }



}
