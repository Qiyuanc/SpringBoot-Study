## SpringBoot 数据校验及多环境配置

接上节，本节补充一下数据校验及多环境配置的内容，仍是 SpringBoot-02-Config 项目。

### 1. 数据校验

使用数据校验，可以在输入不合法数据时抛出异常，首先要添加 `validation` 的依赖

```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <dependency>
        <groupId>javax.validation</groupId>
        <artifactId>validation-api</artifactId>
        <version>2.0.1.Final</version>
    </dependency>
```

在之前的 Person 类上使用 `@Validated` 注解开启数据校验，在 name 属性上添加 `@Email` 注解，表明这个属性要符合 Email 的格式

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component //注册为 bean
@ConfigurationProperties(prefix = "person")
// 开启数据校验
@Validated
public class Person {
    // 检查 name 符合邮箱格式
    @Email()
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}

```

配置文件中注入的 name 属性为 qiyuan，是不合法的，这时运行测试方法，SpringBoot 会报错

```java
Binding to target org.springframework.boot.context.properties.bind.BindException: Failed to bind properties under 'person' to com.qiyuan.entity.Person failed:

    Property: person.name
    Value: qiyuan
    Origin: class path resource [application.yaml] - 2:9
    Reason: 不是一个合法的电子邮件地址
```

查看底层的错误，也可以看到

```java
Caused by: org.springframework.boot.context.properties.bind.validation.BindValidationException: Binding validation errors on person
   - Field error in object 'person' on field 'name': rejected value [qiyuan]; codes [Email.person.name,Email.name,Email.java.lang.String,Email]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [person.name,name]; arguments []; default message [name],[Ljavax.validation.constraints.Pattern$Flag;@44f3fe83,.*]; default message [不是一个合法的电子邮件地址]; origin class path resource [application.yaml] - 2:9
```

总而言之，使用数据校验可以方便地对属性的值进行合法性检测，在 **JSR303** 规范中（ Java Specification Requests，即 Java 规范提案，JSR-303 是 JAVA EE 6 中的一项子规范）还有许多这样的检测注释，用到的时候再查吧！

### 2. 多环境配置

在 Spring 中可以使用 profile 对不同的环境进行不同的配置设置，通过激活不同的环境版本，实现快速切换环境。

在编写配置文件的时候，文件名可以是 ` application-{profile}.properties/yml`，通过不同的 profile 指定不同的环境，如 `application-test.properties` 表示测试环境，`application-dev.properties` 表示开发环境；但 SpringBoot 不会直接使用这种配置文件，它默认使用的是 `application.properties` 配置文件，所以需要指定需要使用的环境

```properties
spring.profiles.active=dev
```

若使用 yaml 进行配置，则更加简单了；yaml 提供了多文档块功能，不用创建多个配置文件

```yaml
server:
  port: 8081
#选择要激活那个环境块
spring:
  profiles:
    active: test
    
---
server:
  port: 8082
spring:
  profiles: dev #配置环境的名称

---
server:
  port: 8083
spring:
  profiles: prod #配置环境的名称
```

**注意**：如果 properties 和 yaml 都进行了端口配置，且没有指定其他配置，会默认使用 properties 配置文件。

### 3. 配置文件加载位置

SpringBoot 会扫描以下位置的 `application.properties` 或 `application.yml` 文件作为默认配置文件，优先级顺序为

1. 项目路径下的 config 文件夹中的配置文件：`file:./config/`
2. 项目路径下的配置文件：`file:./`
3. 资源路径下的 config 文件夹中的配置文件：`classpath:./config/`
4. 资源路径下的配置文件：`classpath:./`

优先级由高到底，高优先级的配置会覆盖低优先级的配置；若没有冲突，则配置会互补！

### 4. 总结

这节就是一些细节的总结，后面也不一定用到到吧，了解了解就行🤨。