## SpringBoot 员工管理系统②

### 1. 登录功能实现

接上回，现在才进入项目的主体部分，首先需要完成一个登录模块。

先修改前端页面，将登录按钮的动作修改为发起 `/user/login` 请求

```html
<form class="form-signin" th:action="@{/user/login}">
```

然后创建 `LoginController` 处理这个请求，先返回一个测试数据查看是否能成功跳转

```java
@Controller
public class LoginController {

    @RequestMapping("/user/login")
    @ResponseBody
    public String login(){
        return "Login!";
    }
}
```

确认这个请求没有问题后，继续修改前端，为用户名和密码输入框提交的参数设置 name

```html
<input type="text" name="username" class="form-control" th:placeholder="#{login.username}" required="" autofocus="">
<input type="password" name="password" class="form-control" th:placeholder="#{login.password}" required="">
```

然后修改控制器，接收参数并用参数处理请求

```java
@Controller
public class LoginController {

    @RequestMapping("/user/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        Model model){
        // 简单的登录判断
        if(!StringUtils.isEmpty(username) && "0723".equals(password))
            // 返回视图 dashboard.html
            return "dashboard";
        else{
            model.addAttribute("msg","用户名或密码错误");
            return "index";
        }
    }
}
```

至此登录模块基本的功能就有了，不过还没有让前端页面显示登录失败的信息，在前端页面中添加

```html
<p style="color: red" th:text="${msg}"></p>
```

通过 Thymeleaf 的 `${}` 就能获取到模型中设置的信息了！

不过按照上面的方式，登录进入主页后地址栏仍是登录时发起的请求的路径，还带着请求的参数，不仅不好看还不安全。这就又要用到自定义 MVC 中的视图解析器了！

首先设置表单提交的方式为 post，消除地址栏中的参数！

```html
<form method="post" class="form-signin" th:action="@{/user/login}">
```

然后修改控制器视图的跳转，由之前的直接交给视图解析器改为重定向

```java
        if(!StringUtils.isEmpty(username) && "0723".equals(password))
            // return "dashboard";
            return "redirect:/dashboard";
        else{
            model.addAttribute("msg","用户名或密码错误");
            // 首页用 post 方式发起请求即可
            return "index";
        }
```

修改为重定向后，若登录成功则会重定向到 `/dashboard` 请求了，我们需要让这个请求能进入注解，即在 `MyMvcConfig` 的配置中为这个请求设置视图解析器

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
        registry.addViewController("/index.html").setViewName("index.html");
        // 为这个请求设置视图解析器
        registry.addViewController("/dashboard").setViewName("dashboard.html");
    }
    ...
}
```

这样登录成功进入的就是 `/dashboard` 请求，会被指向 `dashboard.html` 页面了！

但这样还有一个问题，即使没有登录也能通过 `/dashboard` 请求进入主页，这就需要用到登录拦截器了！

### 2. 登录拦截器

现在为登录模块添加一个登录拦截器，当未登录的时候不允许进入主页！

在 com.qiyuan.config 包下创建 `LoginHandlerInterceptor` 拦截器，实现 `HandlerInterceptor` 接口

```java
public class LoginHandlerInterceptor implements HandlerInterceptor {
    
}
```

实现其中的 `preHandler` 方法，即请求的预处理

```java
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录成功后应设置有用户的 Session
        Object loginUser = request.getSession().getAttribute("loginUser");
        // 找不到对应的 Session，说明没登录
        if(loginUser==null){
            request.setAttribute("msg","请先登录！");
            // 转发后，由配置的视图解析器跳转
            request.getRequestDispatcher("/index.html").forward(request,response);
            // 请求不放行
            return false;
        }else {
            // 登录成功，放行
            return true;
        }
    }
}
```

通过获取 Session 以判断用户是否登录，所以在之前登录成功的时候，应该在 Session 中写入用户的信息

```java
        if(!StringUtils.isEmpty(username) && "0723".equals(password)){
            // return "dashboard";
            // 向 Session 中写入信息（该方法要添加 HttpSession 参数才行）
            session.setAttribute("loginUser",username);
            return "redirect:/dashboard";
        }
        else{
            model.addAttribute("msg","用户名或密码错误");
            // 首页用 post 方式发起请求即可
            return "index";
        }
```

最后要将拦截器注册到 Spring 中，在 `MyMvcConfig` 中实现 `addInterceptor` 方法，和添加视图解析器一样

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    ...
        
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 添加需要拦截的路径，excludePathPatterns 排除不需要拦截的路径
        // 此处本应排除静态资源的路径，不过不排除也没问题，不知道哪里更新了
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").
                excludePathPatterns("/","/index.html","/user/login","/static/**");
    }
}
```

在添加拦截器时，通过 `addPathPatterns` 添加需要拦截的路径，通过 `excludePathPatterns` 排除不需要拦截的路径，非常的人性化！

至此，在未登录的情况下访问 `localhost:8080/dashboard`，就会被拦截器拦截，转发到首页并提示请先登录了！

最后，在主页中将显示的名字（ dashboard.html Line.46 ）改为从 Session 中获取的 `loginUser` 

```html
<a class="navbar-brand col-sm-3 col-md-2 mr-0" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">[[${session.loginUser}]]</a>
```

这样一个小小的登录模块也算完成了吧！