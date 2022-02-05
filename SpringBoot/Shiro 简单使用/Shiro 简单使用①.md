## Shiro 简单使用①

尝试在 SpringBoot 框架中使用 Shiro，对应 SpringBoot-08-Shiro 项目。

### 1. 环境搭建

选择 Web 和 Thhtmeleaf 依赖创建项目后，添加首页页面和对应的控制器确保能访问成功即可。

在 SpringBoot 中添加 Shiro 的依赖，依旧是一个 Starter

```xml
<!-- https://mvnrepository.com/artifact/org.apache.shiro/shiro-spring-boot-web-starter -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring-boot-web-starter</artifactId>
    <version>1.8.0</version>
</dependency>
```

导入完成后，和使用 SpringSecurity 一样需要一个对应的配置类，在 com.qiyuan.config 包下添加 ShiroConfig 配置类

```java
@Configuration
public class ShiroConfig {
    // Shiro 需要的三个关键对象

    // ShiroFilterFactoryBean 
    // DefaultWebSecurityManager 对应 SecurityManager
    // 创建 Realm 对象 需要自定义类 对应 Reaml
}
```

这个配置类中需要配置三个关键对象，这里从底层开始配置。

首先是 Reaml，即一个安全相关的 DAO 层，需要创建 UserRealm 类，也在 config 包下

```java
// 自定义的 Realm 需要继承 AuthorizingRealm 并实现其中的方法
public class UserRealm extends AuthorizingRealm {
    @Override
    // 授权信息
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
        System.out.println("执行了 授权 方法 doGetAuthorizationInfo！");
        return null;
    }

    @Override
    // 认证信息
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了 认证 方法 doGetAuthenticationInfo！");
        return null;
    }
}
```

**自定义的 Realm 类需要继承 AuthorizingRealm 类并实现其中的抽象方法，即授权和认证的方法**；此处先让它们简单地执行输出。

有了自定义 Reaml 类之后，就可以回到 ShiroConfig 进行配置了

```java
@Configuration
public class ShiroConfig {
    // Shiro 需要的三个关键对象

    // 3. ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        // 创建 ShiroFilterFactoryBean 对象
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 和 SecurityManager 关联起来
        bean.setSecurityManager(securityManager);
        return bean;
    }

    // 2. DefaultWebSecurityManager 对应 SecurityManager
    @Bean(name = "securityManager")
    // 将 SecurityManager 也交给 Spring 管理
    // @Qualifier 相当于形参上的 @Autowired
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        // 创建 SecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 和 Realm 关联起来，通过参数让 Spring 把 Realm 对象放进来
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    // 1. 创建 Realm 对象 需要自定义类 对应 Reaml
    @Bean
    // @Bean 告诉 Spring，可以从这个方法中获取到一个对象，对象名就是方法名
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
```

其中的关键点有：

1. 创建了三个关键对象：FactoryBean、SecurityManager、Realm；
2. 将这三个对象都交给 Spring 管理（@Bean）；
3. Spring 将它们通过依赖注入的方式组装了起来（@Qualifier），层次为 Realm—SecurityManager—FactoryBean。

其中涉及到 @Bean 的使用，参考自

> [Spring中Bean及@Bean的理解](https://www.cnblogs.com/bossen/p/5824067.html)

这样 ShiroConfig 就配置好了。

最后再创建两个前端页面 add.html 和 update.html，在首页添加这两个页面的请求，然后在控制器中加上请求的处理，运行测试访问成功即可。

### 2. 登录拦截

现在向 Shiro 中添加登录拦截功能，让未认证的用户不能随便访问！

要使用登录拦截，只需要在 ShiroFilterFactoryBean 中添加内置过滤器，设置访问某资源需要的角色或权限即可

```java
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        // 创建 ShiroFilterFactoryBean 对象
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 和 SecurityManager 关联起来
        bean.setSecurityManager(securityManager);
        /* 权限类别
        *  anon：无需认证即可访问
        *  authc：必须认证才可访问
        *  user：必须有 记住我 功能才能访问
        *  perms:拥有对某个资源的权限才能访问
        *  roles：拥有某个角色权限才能访问
        * */
        // 设置内置过滤器
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 和 Druid 监控设置一样，在 Map 中设置好权限
        filterMap.put("/user/add","authc");
        filterMap.put("/user/update","authc");

        // 也可以使用通配符
        // filterMap.put("/user/*","authc");
        
        bean.setFilterChainDefinitionMap(filterMap);
        // 无权限前往登录页，同 SpringSecurity！
        bean.setLoginUrl("/toLogin");

        return bean;
    }
    ...

}

```

通过在 Map 中设置好资源与其对应需要的权限，再将这个 Map 设置给 ShiroFilterFactoryBean 以实现认证管理！此时首页仍能正常访问，而 add 和 update 页面因设置为需认证才可访问，已经进不去了。

为了正常登陆，当然需要一个登录页面，简单地用表单提交数据即可，然后在控制器中设置 `/toLogin` 请求为前往登录页面，此时进入无权限的页面就会自动跳转到登录页了。

### 3. 用户认证

实现了登录拦截后，还要做用户认证，让认证成功的用户正常访问资源！

**在 Shiro 中，用户认证的操作是在 Realm 中进行的**。不过在此之前，需要将登录操作交给 Shiro

```java
@Controller
public class MyController {
    
    ...

    @RequestMapping("/login")
    public String login(String username,String password,Model model){

        // 获取当前登录的用户对象
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的登录数据为 token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try{
            // 当前用户 带着登录数据 执行登录操作！
            // 没有异常，说明登录成功，出现什么异常就是什么问题！
            subject.login(token);
            return "index";
        }catch (UnknownAccountException unknownAccountException){
            // 不存在用户名异常
            model.addAttribute("msg","用户名不存在！");
            return "login";
        }catch (IncorrectCredentialsException incorrectCredentialsException){
            // 身份验证错误异常 密码错误！
            model.addAttribute("msg","密码错误！");
            return "login";
        }

    }
}
```

控制器中处理登录请求的方法做了三件事（目前）：

1. 获取当前登录的对象 Subject；
2. 封装当前对象的登录数据为 Token；
3. 登录对象带着登录数据执行登录操作 `subject.login(token)`。

此时进入页面输入用户名密码进行登录操作，页面会显示用户名不存在，同时控制台显示

```java
// 执行了 认证 方法 doGetAuthenticationInfo！
```

这就说明之前 UserRealm 类中的认证方法执行了！即认证操作实际发生在 UserRealm 类中，要在其中添加具体的认证逻辑！

```java
// 自定义的 Realm 需要继承 AuthorizingRealm 并实现其中的方法
public class UserRealm extends AuthorizingRealm {
    @Override
    // 授权信息
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
        System.out.println("执行了 授权 方法 doGetAuthorizationInfo！");
        return null;
    }

    @Override
    // 认证信息
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了 认证 方法 doGetAuthenticationInfo！");
        // 先用设置好的数据模拟一下
        String username = "qiyuanc";
        String password = "0723";
        // 将 Token 参数转换为我们需要的类型
        UsernamePasswordToken upToken = (UsernamePasswordToken)token;
        // 用户名认证
        if(!upToken.getUsername().equals(username)){
            // 抛出 UnknowAccountException 异常！
            return null;
        }
        // 密码认证，直接交给 Shiro 做！
        return new SimpleAuthenticationInfo("",password,"");
    }
}
```

此处的认证逻辑为：

1. 获取持久层的登录信息（这里用设置好的数据模拟）；
2. 获取控制器中封装好的用户数据 Token；
3. 进行用户名验证，若不存在则抛出 UnknowAccountException 异常；
4. 把密码验证交给 Shiro，直接返回认证信息对象，若返回成功说明登录成功，返回对象为空说明密码错误，抛出 IncorrectCredentialsException 异常。

其中的关键点在于，控制器封装的 Token 其实是全局的，在 Realm 类中进行认证可以直接获取到！

测试认证功能成功，就可以进入下一阶段了！
