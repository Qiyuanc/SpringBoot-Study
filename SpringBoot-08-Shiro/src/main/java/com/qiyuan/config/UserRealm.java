package com.qiyuan.config;

import com.qiyuan.entity.User;
import com.qiyuan.service.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName UserReaml
 * @Description TODO
 * @Author Qiyuan
 * @Date 2022/1/22 13:51
 * @Version 1.0
 **/
// 自定义的 Realm 需要继承 AuthorizingRealm 并实现其中的方法
public class UserRealm extends AuthorizingRealm {

    // 注入 Service 层，执行业务！
    @Autowired
    UserServiceImpl userService;

    @Override
    // 授权信息
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
        System.out.println("执行了 授权 方法 doGetAuthorizationInfo！");
        // 授权信息对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 获取当前执行操作的用户
        Subject subject = SecurityUtils.getSubject();
        // 获取认证方法中设置的 principal 参数，即 User 对象
        User currentUser = (User) subject.getPrincipal();
        // 添加授权信息
        info.addStringPermission(currentUser.getPerms());
        // 返回授权信息对象
        return info;
    }

    @Override
    // 认证信息
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了 认证 方法 doGetAuthenticationInfo！");
        // 将 Token 参数转换为我们需要的类型
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        // 在数据库中进行查询
        User user = userService.queryUserByName(upToken.getUsername());
        // 用户名认证
        if( user==null ){
            // 查询不到，抛出 UnknowAccountException 异常！
            return null;
        }
        // 密码认证，直接交给 Shiro 做！
        // 将 principal 参数设置为 user，subject 就可以获取到！
        return new SimpleAuthenticationInfo(user,user.getPwd(),"");
    }
}
