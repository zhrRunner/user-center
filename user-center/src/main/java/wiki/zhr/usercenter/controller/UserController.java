package wiki.zhr.usercenter.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import wiki.zhr.usercenter.common.BaseResponse;
import wiki.zhr.usercenter.common.ErrorCode;
import wiki.zhr.usercenter.common.Result;
import wiki.zhr.usercenter.common.ResultUtils;
import wiki.zhr.usercenter.constant.UserConstant;
import wiki.zhr.usercenter.exception.BusinessException;
import wiki.zhr.usercenter.model.domain.User;
import wiki.zhr.usercenter.model.request.*;
import wiki.zhr.usercenter.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "服务器未接收到前端参数");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号、密码、确认密码都不能为空");
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);

        return ResultUtils.success(id);
    }


    @PostMapping("/login")
    public BaseResponse<User> useLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return  ResultUtils.success(user);
    }

    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<User> users = userService.searchUsers(userSearchRequest);
        log.info("查询到了{}个用户", users.size());
        return ResultUtils.success(users);
    }

    @PostMapping("/reset-password")
    public BaseResponse<Boolean> resetPassword(@RequestBody Map<String, Long> requestBody, HttpServletRequest request) {
        // 获取 requestBody 中的 id
        Long id = requestBody.get("id");

        if (!userService.isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }

        boolean reset = userService.resetPassword(id);
        return ResultUtils.success(reset);
    }

    @PostMapping("/update-userinfo")
    public BaseResponse<User> updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request){
        // 暂时只有管理员可以访问，后续用户自己更改信息接口再考虑是否开放 TODO
        if(!userService.isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }

        User user = userService.updateUserInfo(userUpdateRequest);
        return ResultUtils.success(user);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request){

        if(!userService.isAdmin(request)) {
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }

        boolean isFind = userService.deleteUser(userDeleteRequest.getId());
        return ResultUtils.success(isFind);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN, "当前用户未登录");
        }
        // 这里不直接从session缓存里查询而是走数据库
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }



}
