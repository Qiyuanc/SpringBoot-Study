## SpringBoot YAML配置

又回来学习 SpringBoot 啦，本节对应项目 SpringBoot-02-Config！

### 1. YAML简介

YAML 是 "YAML Ain't a Markup Language"（ YAML 不是一种标记语言）的递归缩写。在开发的这种语言时，YAML 的意思其实是 "Yet Another Markup Language"（仍是一种标记语言），但为了强调**这种语言以数据为中心，而不是以标记语言为重点**，而用反向缩略语重命名。

以前使用 XML 来进行配置的时候，如一个端口配置，要写为

```xml
<server>
    <port>8081<port>
</server>
```

若使用 YAML 来配置，则表示为

```yaml
server:
  prot: 8081
```

可以看出相比 XML，YAML 对于数据的配置更加直观，易于理解。

而 SpringBoot 有两种方式进行配置，即之前使用的 application.properties 和现在新学的 application.yml

1. `application.properties`，语法格式为 key=value

   ```properties
   #项目端口号
   server.port=8081
   #项目路径
   server.servlet.context-path=/SpringBoot-01-Hello

2. `application.yml`，语法格式为 key : 空格 value

   ```yaml
   server:
     prot: 8081
   ```

一般来说还是 YAML 配置使用的比较多！

### 2. YAML语法

YAML 基本语法

1. 格式为  key : **空格** value，注意，空格不能省略
2. 以缩进来控制层级关系，与 Python 类似
3. 属性和值都对大小写敏感

**字面量：普通的值（数字、布尔值、字符串）**

直接按照 key : 空格 value 的格式写即可，字符串可以不加双引号和单引号，如

```yaml
num: value
bool: true
string: this is string
string: "this is \n string"
string: 'this is \n string'
```

双引号：不会转义字符串里面的特殊字符，如上面加双引号后的字符串为 `this is 换行 string`。

单引号：会转义字符串里面的特殊字符，如上面加单引号后的字符串为 `this is \n string`。

**对象、Map（键值对）**

可以采用缩进表明从属关系，如

```yaml
key:
	value1:
	value2:
```

```yaml
student:
    name: qiyuan
    age: 18
```

也可以写在一行内，如

```yaml
student: {name: qiyuan,age: 18}
```

**数组（ List、Set ）**

用 - 值表示数组中的一个元素，如

```yaml
Animals:
 - Cat
 - Dog
 - Pig
```

也可以写在一行内，如

```yaml
Animals: [Cat,Dog,Pig]
```

**修改配置信息**

直接给存在的配置信息设置新的值即可

```yaml
server:
  port: 8082
```

简单的 YAML 语法就了解这么多，下面进行一下实际使用！

### 3. YAML注入配置

YAML 真正强大的地方在于它可以用来给实体类注入属性和依赖！

#### 3.1 注解方式

在使用 YAML 注入前，先复习一下使用 Spring 注解的注入。

首先在 com.qiyuan.entity 包下编写一个实体类 Dog（使用了 Lombok），将其注册为 bean 并注入属性

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
// 注册为 bean
@Component
public class Dog {
    // 注入属性
    @Value("小狗")
    private String name;
    @Value("3")
    private Integer age;
}
```

在 SpringBoot 自带的测试类中测试一下

```java
@SpringBootTest
class SpringBoot02ConfigApplicationTests {
    @Autowired
    Dog dog;

    @Test
    void contextLoads() {
        System.out.println(dog);
    }
}
// 输出
// Dog(name=小狗, age=3)
```

这就是之前的自动装配的方式了，通过 `@Component` 和 `@Value` 注解实现！

#### 3.2 YAML方式

使用 YAML 顺便来个复杂点的配置，再创建一个实体类 Person

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component //注册为 bean
public class Person {
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
```

在 Resources 目录下的 `application.yaml` 文件中对 Person 进行注入

```yaml
person:
  name: qiyuan
  age: 18
  happy: true
  birth: 2000/01/18
  maps: {key1: value1,key2: value2}
  lists:
    - bbb
    - lol
    - apex
  dog:
    name: "小狗"
    age: 3
```

这样对于的值就写好了，在 Person 类上添加 `@ConfigurationProperties(prefix = "person")` 注解，将配置的信息与类中的属性对应

```java
@ConfigurationProperties(prefix = "person")
public class Person
```

此时 IDEA 会给出提示表示配置注解处理器未找到

```java
// Spring Boot Configuration Annotation Processor not found in classpath
```

需要导入一个依赖（不用好像也能运行吧，反正我导入了），导入完后会提示重启 SpringBoot

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-configuration-processor</artifactId>
      <optional>true</optional>
    </dependency>
```

再去测试类中输出一下看看

```java
@SpringBootTest
class SpringBoot02ConfigApplicationTests {
    @Autowired
    Dog dog;
    @Autowired
    Person person;

    @Test
    void contextLoads() {
        System.out.println(person);
    }
}
// 输出
/*
    Person(name=qiyuan, age=18, happy=true, 
    birth=Tue Jan 18 00:00:00 CST 2000, 
    maps={key1=value1, key2=value2}, 
    lists=[bbb, lol, apex], 
    dog=Dog(name=小狗, age=3))
*/ 
```

使用 yaml 文件注入成功！

#### 3.3 对比Properties方式

上面通过 `@ConfigurationProperties(prefix = "person")` 注解，从默认的配置文件 `application.yaml` 中获取了 person 的配置；如果不想从默认的配置文件中获取，可以使用 `@PropertySource` 注解。

在 resources 目录下创建 `person.properties` 文件，只设置 name 属性的值

```properties
name=qiyuan
```

将 Person 类上的注解改为 `@PropertySource(value = "classpath:person.properties")` ，同时在对应的属性上使用 `@Value` 注解获取对应的值

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component //注册为 bean
//@ConfigurationProperties(prefix = "person")
@PropertySource(value = "classpath:person.properties")
public class Person {
    @Value("${name}")
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
}
```

再次运行上面的测试方法，查看结果

```java
// 输出
// Person(name=qiyuan, age=null, happy=null, birth=null, maps=null, lists=null, dog=null)
```

成功获取了 name 属性的值，其他属性没有设置所以为 null，合理！

不过使用这种方式，每种属性上都要使用 `@Value` 注解获取对应的值，如果属性非常多，则会显得非常杂乱；这就体现出了 yaml 的好处，可以**批量注入**！

### 4. 总结

这节学习了 yaml 的用法，其功能确实比 properties 强大。如果在某个业务中，只需要获取配置文件中的某个值，可以使用 `@value`；不过如果需要获取 JavaBean 的很多属性值，还是使用 `@ConfigurationProperties` 吧😀！