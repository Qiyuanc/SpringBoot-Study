## SpringBoot 自动配置原理

这一节又要从源码分析一下 SpringBoot 的自动配置，和配置文件要怎么写了！

### 1. 回顾运行原理

在之前的 SpringBoot 运行原理中可以知道，SpringBoot 通过 `AutoConfigurationImportSelector` 类实现了自动配置，层级关系为

```java
// 主启动类
@SpringBootApplication
// 开启自动配置
@EnableAutoConfiguration
// 导入自动配置选择器类
@Import({AutoConfigurationImportSelector.class})
// 类中获取候选配置的方法
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes);
// 上面的方法调用了 SpringFactoriesLoader 类的 loadFactoryNames 方法
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader);
// 继续调用了 loadSpringFactories 方法
private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader);
// 方法中从指定的地方加载 URL
Enumeration urls = classLoader.getResources("META-INF/spring.factories");
```

最后是从 `META-INF/spring.factories` 文件中获取到了所有可能会被加载的配置类（候选配置），这个文件在 `spring-boot-autoconfigure-2.5.4.jar` 包下。通过当前 SpringBoot 的运行配置，判断哪些配置类是要被加载的，就实现了自动配置。

### 2. 自动配置原理

在 `spring.factories` 文件中找到 `HttpEncodingAutoConfiguration` 类（ Http 编码自动配置），用它来继续分析自动配置的原理（因为这个配置类比较短）

`HttpEncodingAutoConfiguration` 类的部分代码，通过注释标明了这些注解的作用

```java
// 表明这是一个配置类
@Configuration(proxyBeanMethods = false)
// 开启对应类的 ConfigurationProperties 功能
@EnableConfigurationProperties(ServerProperties.class)
// 这就是之前提到的 @ConditinalOn... 注解了，判断是否符合加载的条件
// 判断当前是否为 Web 应用
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
// 判断当前项目是否有 CharacterEncodingFilter 类
@ConditionalOnClass(CharacterEncodingFilter.class)
// 判断配置文件中是否存在 server.servlet.encoding 配置，默认生效
@ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)
public class HttpEncodingAutoConfiguration {

	private final Encoding properties;

	public HttpEncodingAutoConfiguration(ServerProperties properties) {
		this.properties = properties.getServlet().getEncoding();
	}

    // 给容器添加一个组件（bean），其中的值可能从 properties 中获取
	@Bean
    // 判断容器中是否存在这个组件（bean）
	@ConditionalOnMissingBean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.properties.getCharset().name());
		filter.setForceRequestEncoding(this.properties.shouldForce(Encoding.Type.REQUEST));
		filter.setForceResponseEncoding(this.properties.shouldForce(Encoding.Type.RESPONSE));
		return filter;
	}

	...
}

```

其中值得特别注意的是 `@EnableConfigurationProperties(ServerProperties.class)` 这个注解，它对应了一个属性类 `ServerProperties`，点进去看看它是什么东西

```java
// 之前使用的 @ConfigurationProperties 注解！它会去寻找配置文件中 server 的配置！
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
public class ServerProperties {

	/**
	 * Server HTTP port.
	 */
	private Integer port;

	/**
	 * Network address to which the server should bind.
	 */
	private InetAddress address;
    
    // 还有很多很多属性...
    
}
```

可以看到之前在 YAML 配置中使用的 `@ConfigurationProperties` 注解，这个注解表明，**这个类中的属性会与配置文件中前缀为 server 的属性进行对应！也就是说，这个类中的属性，就是我们能在配置文件中以 server 前缀配置的属性！**

<img src="../SpringBoot自动配置原理/image-20210919131102548.png" alt="image-20210919131102548" style="zoom: 80%;" />

通过查看配置类（ `HttpEncodingAutoConfiguration` ）对应的属性类（ `ServerProperties` ）中的属性与绑定的前缀（ `prefix = "server"` ），就可以知道在配置文件中能进行哪些配置了；如果不进行配置，则属性类会采用默认的值，**约定大于配置**！

### 3. 总结

**SpringBoot 自动配置过程总结**：

1. SpringBoot 在启动时会加载大量的**候选配置类**（ `xxxAutoConfiguration` ）；
2. 根据当前场景条件判断（ `@ConditionalOn...` ），某个配置类是否会生效！
3. 如果配置类生效，这个配置类就会给容器添加组件；
4. 这些组件的属性都会从对应的 Properties 类（ `xxxProperties` ）中获取；
5. 配置文件中能进行的配置都是 Properties 类的属性；
6. 通过参照 Properties 类的属性，就可以知道配置文件能进行什么配置！

对原理的研究就差不多到这吧，还是得学习学习怎么使用😪！