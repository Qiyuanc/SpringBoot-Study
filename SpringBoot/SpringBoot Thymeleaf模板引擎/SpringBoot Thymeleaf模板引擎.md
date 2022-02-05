## SpringBoot Thymeleaf模板引擎

本节学习一下 SpringBoot 中的模板引擎，以 Thymeleaf 为例，仍在 SpringBoot-03-Web 项目中！

### 1. Thymeleaf简介

首先明确一下模板引擎的概念：模板引擎是为了使用户界面与业务数据（内容）分离而产生的，它可以生成特定格式的文档，用于网站的模板引擎就会生成一个标准的 HTML 文档。

简单来说，之前用的 JSP 也算一种模板引擎（大概），它的作用就是获取数据（通过 `${}` 表达式）并展示；后台将什么数据给它，它取出来展示的就是什么数据，即类似于一个模板，可以向其中填充不同的内容！

Thymeleaf 是一个现代服务器端的 Java 模板引擎，适用于Web和独立环境，能够处理 HTML、XML、JavaScript、CSS 甚至纯文本，它可以完全替代 JSP ！

### 2. Thymeleaf视图解析

要使用 Thymeleaf，需要导入对应的依赖，此处直接使用 SpringBoot-Starter 导入，简单方便！

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
        <version>2.5.4</version>
    </dependency>
```

导入之后的东西肯定有其对应的配置，在之前的自动配置原理中我们知道，任何配置都对应了一个属性类，Thymeleaf 也不例外，查看 `ThymeleafProperties` 类

```java
public class ThymeleafProperties {

	private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".html";

    ...
}
```

首先只看前面三个属性，`DEFAULT_ENCODING` 为默认编码格式，此处为 UTF-8；`DEFAULT_PREFIX` 为默认前缀，此处为 `classpath:/templates/`，`DEFAULT_SUFFIX` 为默认后缀，此处为 `.html`。这前缀和后缀，有点熟悉，不就是之前 SpringMVC 中的视图解析器吗！

前缀即视图所在的位置，这里为**类路径下的 `templates` 目录，这个目录类似之前 Web 项目中的 WEB-INF 目录，必须要通过控制器重定向才能访问到**；后缀即视图的格式，以前在 SpringMVC 中设置的是 JSP，这里默认为 HTML！

简单测试一下，先在 `templates` 目录下创建一个 `hello.html` 文件，内容就是输出一下 `Hello,world!`；然后编写一个控制器 `HelloController`，通过 `/hello` 请求跳转到 `hello.html`

```java
@Controller
public class HelloController {
    
    @RequestMapping("/hello")
    public String Hello(){
        return "hello";
    }

}
```

访问 `http://localhost:8080/hello`，跳转成功，这就是 Thymeleaf 模板引擎的作用之一，充当了视图解析器！

### 3. Thymeleaf使用

上面只是探究了一下 Thymeleaf 中的视图解析，还没有真正使用到 Thymeleaf。要在页面中使用 Thymeleaf，需要先引入约束

```html
<html xmlns:th="http://www.thymeleaf.org">
```

然后在页面中就可以 `${}` 获取数据了（**注意要使用 th 接管元素**），这里尝试在 `hello.html` 中获取后台设置的 msg 数据

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Hello</title>
</head>
<body>
<!--所有的 HTML 元素都可以使用 th 去替换接管！-->
<div th:text="${msg}"></div>
</body>
</html>
```

在控制器中，设置对应的模型数据

```java
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String Hello(Model model){
        model.addAttribute("msg","Hello,SpringBoot and Thymeleaf!");
        return "hello";
    }

}
```

访问 `http://localhost:8080/hello`，成功显示出我们设置好的数据，Thymeleaf 使用成功！

### 4. Thymeleaf语法

再简单学习一下 Thymeleaf 的语法，具体的以后用到再说！

**基本表达式**

1. 变量（ Variable ）：`${...}`
2. 选择变量（ Selection Variable ）：`*{...}`
3. 信息（ Message ）：`#{...}`
4. URL 链接（ Link URL ）：`@{...}`
5. 片段（ Fragment ）：`~{...}`（不知道干什么的）

**不转义文本 utext**

上面使用的 `th:text` 会将内容转义输出，而 `th:utext` 不会，用例子说明比较直观

```html
<div th:text="${msg}"></div>
<div th:utext="${msg}"></div>
```

 ```java
 model.addAttribute("msg","<h1>Hello,SpringBoot and Thymeleaf!</h1>");
 ```

使用 text 会输出 `<h1>Hello,SpringBoot and Thymeleaf!</h1>`，而 `utext` 会输出 h1 格式的 `Hello,SpringBoot and Thymeleaf!`。

**遍历 each**

使用 each 可以遍历元素，如

```html
<h2 th:each="user:${users}" th:text="${user}"></h2>
```

```java
model.addAttribute("users", Arrays.asList("Qiyuan","Qiyuanc"));
```

这种标签类的东西还是用到的时候再看吧，现在看了也没什么用！

### 5. 总结

这节稍微学习了一下以 Thymeleaf 为例的模板引擎，总而言之就是相当于 JSP 的作用了；不过模板引擎属于后端，而使用 Vue 则属于前端，那为什么不在前端用 Vue 呢？不是很懂(・∀・(・∀・(・∀・*)。

