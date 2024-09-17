package wiki.zhr.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import wiki.zhr.usercenter.common.ErrorCode;
import wiki.zhr.usercenter.constant.UserConstant;
import wiki.zhr.usercenter.exception.BusinessException;
import wiki.zhr.usercenter.model.domain.User;
import wiki.zhr.usercenter.service.UserService;
import wiki.zhr.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hrz
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-09-07 10:12:46
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /*
     * 混淆密码用，盐值
     */
    private static final String SALT = "zhr";



    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号小于4位");
        }

        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码小于4位");
        }

        // 账户不包含特殊字符 (正则表达式)
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {  // 如果找到特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能包含特殊字符");
        }

        // 密码是否和确认密码一致
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该账号已被注册，请重新更换账号");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 保存 （向数据库插入一条数据）
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "服务器保存数据失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验用户账户
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码不能为空");
        }

        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位");
        }

        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号密码小于8位");
        }

        // 账户不包含特殊字符 (正则表达式)
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {  // 如果找到特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }

        // 校验密码是否输入正确
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        // 用户不存在
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "对不起，该用户不存在或密码错误");
        }

        // 2. 返回用户信息(脱敏)
        User safetyUser = getSafetyUser(user);

        // 3. 记录用户的的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public List<User> searchUsers(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isAnyBlank(username)){
            // 判断username是否为空或者null
            queryWrapper.like("username", username);
        }
        List<User> users = this.list(queryWrapper);

        // 返回用户信息时对密码进行脱敏
        return users.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteUser(long id) {
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户不存在或已被封禁");
        }
        return this.removeById(id);
    }


    @Override
    public User getSafetyUser(User originUser) {
        if(originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());

        return safetyUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request){
        // 鉴权 ---- 仅管理员才能访问的接口
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user == null || user.getUserRole() != UserConstant.ADMIN_ROLE){
            return false;
        }
        return true;
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 获取当前 session
        HttpSession session = request.getSession();

        // 移除 session 中的用户登录状态
        session.removeAttribute(UserConstant.USER_LOGIN_STATE);

        return 1;
    }

    @Override
    public boolean resetPassword(Long id) {
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);

        // 如果用户不存在，返回 false
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该用户不存在或已被封禁");
        }

        // 重置密码
        String defaultPassword = "12345678";
        // 如果需要加密密码，这里可以使用加密函数
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());

        // 更新用户的密码
        user.setUserPassword(encryptPassword);

        // 更新用户信息
        int result = userMapper.updateById(user);

        // 如果更新成功，返回 true，否则返回 false
        return result > 0;
    }
}





